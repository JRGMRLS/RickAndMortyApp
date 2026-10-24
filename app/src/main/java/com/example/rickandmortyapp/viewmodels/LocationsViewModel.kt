package com.example.rickandmortyapp.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmortyapp.Location
import com.example.rickandmortyapp.data.local.database.AppDatabase
import com.example.rickandmortyapp.data.repository.LocationRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class LocationsUiState(
    val isLoading: Boolean = true,
    val data: List<Location> = emptyList(),
    val hasError: Boolean = false
)

class LocationsViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val database =
        AppDatabase.getDatabase(application.applicationContext)

    private val repository =
        LocationRepository(database.locationDao())

    private val _uiState = MutableStateFlow(
        LocationsUiState()
    )

    val uiState: StateFlow<LocationsUiState> =
        _uiState.asStateFlow()

    private var locationsJob: Job? = null

    init {
        loadLocations()
    }

    fun loadLocations() {
        locationsJob?.cancel()

        _uiState.value = LocationsUiState(
            isLoading = true
        )

        locationsJob = viewModelScope.launch {
            delay(4000)

            repository
                .getAllLocations()
                .collect { locations ->
                    _uiState.value = LocationsUiState(
                        isLoading = false,
                        data = locations,
                        hasError = false
                    )
                }
        }
    }

    fun showError() {
        if (_uiState.value.isLoading) {
            locationsJob?.cancel()

            _uiState.value = _uiState.value.copy(
                isLoading = false,
                hasError = true
            )
        }
    }
}