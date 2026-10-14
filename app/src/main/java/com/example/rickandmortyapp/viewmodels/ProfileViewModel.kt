package com.example.rickandmortyapp.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmortyapp.data.preferences.UserPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ProfileUiState(
    val isLoading: Boolean = true,
    val name: String = ""
)

class ProfileViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val userPreferences =
        UserPreferences(application.applicationContext)

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
                _uiState.value = ProfileUiState(
                    isLoading = false,
                    name = savedName.orEmpty()
                )
            }
        }
    }

    fun logout(
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            userPreferences.clearName()
            onSuccess()
        }
    }
}