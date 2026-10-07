package com.example.rickandmortyapp.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.rickandmortyapp.Character
import com.example.rickandmortyapp.CharacterDb
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
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val characterDb = CharacterDb()

    private val characterId =
        savedStateHandle
            .toRoute<CharacterDetailsDestination>()
            .id

    private val _uiState = MutableStateFlow(
        CharacterDetailsUiState()
    )

    val uiState: StateFlow<CharacterDetailsUiState> =
        _uiState.asStateFlow()

    private var loadingJob: Job? = null

    init {
        loadCharacter()
    }

    fun loadCharacter() {
        loadingJob?.cancel()

        _uiState.value = CharacterDetailsUiState(
            isLoading = true
        )

        loadingJob = viewModelScope.launch {
            delay(2000)

            _uiState.value = CharacterDetailsUiState(
                isLoading = false,
                data = characterDb.getCharacterById(characterId),
                hasError = false
            )
        }
    }

    fun showError() {
        if (_uiState.value.isLoading) {
            loadingJob?.cancel()

            _uiState.value = _uiState.value.copy(
                isLoading = false,
                hasError = true
            )
        }
    }
}