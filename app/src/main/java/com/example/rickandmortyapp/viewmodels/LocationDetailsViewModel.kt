package com.example.rickandmortyapp.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.rickandmortyapp.Location
import com.example.rickandmortyapp.data.local.database.AppDatabase
import com.example.rickandmortyapp.data.repository.LocationRepository
import com.example.rickandmortyapp.navigation.LocationDetailsDestination
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class LocationDetailsUiState(
    val isLoading: Boolean = true,
    val data: Location? = null,
    val hasError: Boolean = false
)

class LocationDetailsViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private val locationId =
        savedStateHandle
            .toRoute<LocationDetailsDestination>()
            .id

    private val database =
        AppDatabase.getDatabase(application.applicationContext)

    private val repository =
        LocationRepository(database.locationDao())

    private val _uiState = MutableStateFlow(
        LocationDetailsUiState()
    )

    val uiState: StateFlow<LocationDetailsUiState> =
        _uiState.asStateFlow()

    private var locationJob: Job? = null

    init {
        loadLocation()
    }

    fun loadLocation() {
        locationJob?.cancel()

        _uiState.value = LocationDetailsUiState(
            isLoading = true
        )

        locationJob = viewModelScope.launch {
            delay(2000)

            repository
                .getLocationById(locationId)
                .collect { location ->
                    _uiState.value = LocationDetailsUiState(
                        isLoading = false,
                        data = location,
                        hasError = false
                    )
                }
        }
    }

    fun showError() {
        if (_uiState.value.isLoading) {
            locationJob?.cancel()

            _uiState.value = _uiState.value.copy(
                isLoading = false,
                hasError = true
            )
        }
    }
}