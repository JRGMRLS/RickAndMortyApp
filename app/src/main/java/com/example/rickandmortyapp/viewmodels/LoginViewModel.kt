package com.example.rickandmortyapp.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmortyapp.data.local.database.AppDatabase
import com.example.rickandmortyapp.data.preferences.UserPreferences
import com.example.rickandmortyapp.data.repository.SyncRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class LoginUiState(
    val name: String = "",
    val isSaving: Boolean = false,
    val hasError: Boolean = false
)

class LoginViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val userPreferences =
        UserPreferences(application.applicationContext)

    private val database =
        AppDatabase.getDatabase(application.applicationContext)

    private val syncRepository =
        SyncRepository(database)

    private val _uiState = MutableStateFlow(
        LoginUiState()
    )

    val uiState: StateFlow<LoginUiState> =
        _uiState.asStateFlow()

    fun onNameChange(name: String) {
        _uiState.value = _uiState.value.copy(
            name = name,
            hasError = false
        )
    }

    fun login(
        onSuccess: () -> Unit
    ) {
        val name = _uiState.value.name.trim()

        if (name.isBlank()) {
            _uiState.value = _uiState.value.copy(
                hasError = true
            )
            return
        }

        if (_uiState.value.isSaving) {
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isSaving = true,
                hasError = false
            )

            syncRepository.synchronizeInitialData()

            userPreferences.saveName(name)

            _uiState.value = _uiState.value.copy(
                name = name,
                isSaving = false
            )

            onSuccess()
        }
    }
}