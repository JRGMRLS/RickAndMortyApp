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

enum class LocationFilter {
    ALL,
    FAVORITES,
    NON_FAVORITES
}

data class LocationsUiState(
    val isLoading: Boolean = true,
    val data: List<Location> = emptyList(),
    val hasError: Boolean = false,
    val filter: LocationFilter = LocationFilter.ALL
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
        observeLocations(
            showLoading = true
        )
    }

    private fun observeLocations(
        showLoading: Boolean
    ) {
        locationsJob?.cancel()

        _uiState.value = _uiState.value.copy(
            isLoading = showLoading,
            data = emptyList(),
            hasError = false
        )

        locationsJob = viewModelScope.launch {
            if (showLoading) {
                delay(4000)
            }

            val locationsFlow =
                when (_uiState.value.filter) {
                    LocationFilter.ALL ->
                        repository.getAllLocations()

                    LocationFilter.FAVORITES ->
                        repository.getFavoriteLocations()

                    LocationFilter.NON_FAVORITES ->
                        repository.getNonFavoriteLocations()
                }

            locationsFlow.collect { locations ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    data = locations,
                    hasError = false
                )
            }
        }
    }

    fun setFilter(
        filter: LocationFilter
    ) {
        if (_uiState.value.filter == filter) {
            return
        }

        _uiState.value = _uiState.value.copy(
            filter = filter
        )

        observeLocations(
            showLoading = false
        )
    }

    fun toggleFavorite(
        location: Location
    ) {
        viewModelScope.launch {
            repository.updateFavorite(
                id = location.id,
                isFavorite = !location.isFavorite
            )
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