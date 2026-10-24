package com.example.rickandmortyapp.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmortyapp.Character
import com.example.rickandmortyapp.data.local.database.AppDatabase
import com.example.rickandmortyapp.data.repository.CharacterRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CharactersUiState(
    val isLoading: Boolean = true,
    val data: List<Character> = emptyList(),
    val hasError: Boolean = false,
    val showFavoritesOnly: Boolean = false
)

class CharactersViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val database =
        AppDatabase.getDatabase(application.applicationContext)

    private val repository =
        CharacterRepository(database.characterDao())

    private val _uiState = MutableStateFlow(
        CharactersUiState()
    )

    val uiState: StateFlow<CharactersUiState> =
        _uiState.asStateFlow()

    private var charactersJob: Job? = null

    init {
        loadCharacters()
    }

    fun loadCharacters() {
        observeCharacters(
            showLoading = true
        )
    }

    private fun observeCharacters(
        showLoading: Boolean
    ) {
        charactersJob?.cancel()

        _uiState.value = _uiState.value.copy(
            isLoading = showLoading,
            data = emptyList(),
            hasError = false
        )

        charactersJob = viewModelScope.launch {
            if (showLoading) {
                delay(4000)
            }

            val charactersFlow =
                if (_uiState.value.showFavoritesOnly) {
                    repository.getFavoriteCharacters()
                } else {
                    repository.getAllCharacters()
                }

            charactersFlow.collect { characters ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    data = characters,
                    hasError = false
                )
            }
        }
    }

    fun toggleFavoritesFilter() {
        _uiState.value = _uiState.value.copy(
            showFavoritesOnly =
                !_uiState.value.showFavoritesOnly
        )

        observeCharacters(
            showLoading = false
        )
    }

    fun toggleFavorite(
        character: Character
    ) {
        viewModelScope.launch {
            repository.updateFavorite(
                id = character.id,
                isFavorite = !character.isFavorite
            )
        }
    }

    fun showError() {
        if (_uiState.value.isLoading) {
            charactersJob?.cancel()

            _uiState.value = _uiState.value.copy(
                isLoading = false,
                hasError = true
            )
        }
    }
}