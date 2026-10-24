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

data class ProfileUiState(
    val isLoading: Boolean = true,
    val name: String = "",
    val isLoggingOut: Boolean = false
)

class ProfileViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val userPreferences =
        UserPreferences(application.applicationContext)

    private val database =
        AppDatabase.getDatabase(application.applicationContext)

    private val syncRepository =
        SyncRepository(database)

    private val _uiState = MutableStateFlow(
        ProfileUiState()
    )

    val uiState: StateFlow<ProfileUiState> =
        _uiState.asStateFlow()

    init {
        observeUserName()
    }

    private fun observeUserName() {
        viewModelScope.launch {
            userPreferences.userName.collect { savedName ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    name = savedName.orEmpty()
                )
            }
        }
    }

    fun logout(
        onSuccess: () -> Unit
    ) {
        if (_uiState.value.isLoggingOut) {
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoggingOut = true
            )

            syncRepository.clearAllData()
            userPreferences.clearName()

            onSuccess()
        }
    }
}