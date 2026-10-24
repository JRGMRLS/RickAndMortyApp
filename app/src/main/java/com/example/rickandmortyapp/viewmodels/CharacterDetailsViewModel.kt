package com.example.rickandmortyapp.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.rickandmortyapp.Character
import com.example.rickandmortyapp.data.local.database.AppDatabase
import com.example.rickandmortyapp.data.repository.CharacterRepository
import com.example.rickandmortyapp.navigation.CharacterDetailsDestination
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CharacterDetailsUiState(
    val isLoading: Boolean = true,
    val data: Character? = null,
    val hasError: Boolean = false
)

class CharacterDetailsViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private val characterId =
        savedStateHandle
            .toRoute<CharacterDetailsDestination>()
            .id

    private val database =
        AppDatabase.getDatabase(application.applicationContext)

    private val repository =
        CharacterRepository(database.characterDao())

    private val _uiState = MutableStateFlow(
        CharacterDetailsUiState()
    )

    val uiState: StateFlow<CharacterDetailsUiState> =
        _uiState.asStateFlow()

    private var characterJob: Job? = null

    init {
        loadCharacter()
    }

    fun loadCharacter() {
        characterJob?.cancel()

        _uiState.value = CharacterDetailsUiState(
            isLoading = true
        )

        characterJob = viewModelScope.launch {
            delay(2000)

            repository
                .getCharacterById(characterId)
                .collect { character ->
                    _uiState.value = CharacterDetailsUiState(
                        isLoading = false,
                        data = character,
                        hasError = false
                    )
                }
        }
    }

    fun toggleFavorite() {
        val character = _uiState.value.data ?: return

        viewModelScope.launch {
            repository.updateFavorite(
                id = character.id,
                isFavorite = !character.isFavorite
            )
        }
    }

    fun showError() {
        if (_uiState.value.isLoading) {
            characterJob?.cancel()

            _uiState.value = _uiState.value.copy(
                isLoading = false,
                hasError = true
            )
        }
    }
}