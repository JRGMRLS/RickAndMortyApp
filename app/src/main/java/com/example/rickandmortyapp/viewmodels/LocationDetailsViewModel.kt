package com.example.rickandmortyapp.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.rickandmortyapp.Location
import com.example.rickandmortyapp.LocationDb
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
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val locationDb = LocationDb()

    private val locationId =
        savedStateHandle
            .toRoute<LocationDetailsDestination>()
            .id

    private val _uiState = MutableStateFlow(
        LocationDetailsUiState()
    )

    val uiState: StateFlow<LocationDetailsUiState> =
        _uiState.asStateFlow()

    private var loadingJob: Job? = null

    init {
        loadLocation()
    }

    fun loadLocation() {
        loadingJob?.cancel()

        _uiState.value = LocationDetailsUiState(
            isLoading = true
        )

        loadingJob = viewModelScope.launch {
            delay(2000)

            _uiState.value = LocationDetailsUiState(
                isLoading = false,
                data = locationDb.getLocationById(locationId),
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