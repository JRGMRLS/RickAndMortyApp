package com.example.rickandmortyapp.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmortyapp.data.preferences.UserPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class SessionUiState(
    val isLoading: Boolean = true,
    val isLoggedIn: Boolean = false
)

class SessionViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val userPreferences =
        UserPreferences(application.applicationContext)

    private val _uiState = MutableStateFlow(
        SessionUiState()
    )

    val uiState: StateFlow<SessionUiState> =
        _uiState.asStateFlow()

    init {
        checkSession()
    }

    private fun checkSession() {
        viewModelScope.launch {
            val savedName = userPreferences.userName.first()

            _uiState.value = SessionUiState(
                isLoading = false,
                isLoggedIn = !savedName.isNullOrBlank()
            )
        }
    }
}