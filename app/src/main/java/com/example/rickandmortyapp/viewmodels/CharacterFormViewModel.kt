package com.example.rickandmortyapp.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.rickandmortyapp.Character
import com.example.rickandmortyapp.data.local.database.AppDatabase
import com.example.rickandmortyapp.data.repository.CharacterRepository
import com.example.rickandmortyapp.navigation.CharacterFormDestination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class CharacterFormUiState(
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val isEditing: Boolean = false,
    val name: String = "",
    val status: String = "",
    val species: String = "",
    val gender: String = "",
    val image: String = "",
    val errorMessage: String? = null
)

class CharacterFormViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private val characterId =
        savedStateHandle
            .toRoute<CharacterFormDestination>()
            .id

    private val database =
        AppDatabase.getDatabase(application.applicationContext)

    private val repository =
        CharacterRepository(database.characterDao())

    private var originalCharacter: Character? = null

    private val _uiState = MutableStateFlow(
        CharacterFormUiState(
            isLoading = characterId != null,
            isEditing = characterId != null
        )
    )

    val uiState: StateFlow<CharacterFormUiState> =
        _uiState.asStateFlow()

    init {
        loadCharacter()
    }

    private fun loadCharacter() {
        val id = characterId ?: return

        viewModelScope.launch {
            val character =
                repository.getCharacterById(id).first()

            originalCharacter = character

            if (character == null) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "No se encontró el personaje"
                )
                return@launch
            }

            _uiState.value = CharacterFormUiState(
                isLoading = false,
                isEditing = true,
                name = character.name,
                status = character.status,
                species = character.species,
                gender = character.gender,
                image = character.image
            )
        }
    }

    fun onNameChange(value: String) {
        _uiState.value = _uiState.value.copy(
            name = value,
            errorMessage = null
        )
    }

    fun onStatusChange(value: String) {
        _uiState.value = _uiState.value.copy(
            status = value,
            errorMessage = null
        )
    }

    fun onSpeciesChange(value: String) {
        _uiState.value = _uiState.value.copy(
            species = value,
            errorMessage = null
        )
    }

    fun onGenderChange(value: String) {
        _uiState.value = _uiState.value.copy(
            gender = value,
            errorMessage = null
        )
    }

    fun onImageChange(value: String) {
        _uiState.value = _uiState.value.copy(
            image = value,
            errorMessage = null
        )
    }

    fun saveCharacter(
        onSuccess: () -> Unit
    ) {
        val state = _uiState.value

        if (
            state.name.isBlank() ||
            state.status.isBlank() ||
            state.species.isBlank() ||
            state.gender.isBlank()
        ) {
            _uiState.value = state.copy(
                errorMessage =
                    "Completa todos los campos obligatorios"
            )
            return
        }

        if (state.isSaving) {
            return
        }

        viewModelScope.launch {
            _uiState.value = state.copy(
                isSaving = true,
                errorMessage = null
            )

            val character = Character(
                id = originalCharacter?.id ?: 0,
                name = state.name.trim(),
                status = state.status.trim(),
                species = state.species.trim(),
                gender = state.gender.trim(),
                image = state.image.trim(),
                isFavorite =
                    originalCharacter?.isFavorite ?: false
            )

            if (state.isEditing) {
                repository.updateCharacter(character)
            } else {
                repository.createCharacter(character)
            }

            _uiState.value = _uiState.value.copy(
                isSaving = false
            )

            onSuccess()
        }
    }
}