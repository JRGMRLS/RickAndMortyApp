package com.example.rickandmortyapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmortyapp.Location
import com.example.rickandmortyapp.LocationDb
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

class LocationsViewModel : ViewModel() {

    private val locationDb = LocationDb()

    private val _uiState = MutableStateFlow(
        LocationsUiState()
    )

    val uiState: StateFlow<LocationsUiState> =
        _uiState.asStateFlow()

    private var loadingJob: Job? = null

    init {
        loadLocations()
    }

    fun loadLocations() {
        loadingJob?.cancel()

        _uiState.value = LocationsUiState(
            isLoading = true
        )

        loadingJob = viewModelScope.launch {
            delay(4000)

            _uiState.value = LocationsUiState(
                isLoading = false,
                data = locationDb.getAllLocations(),
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