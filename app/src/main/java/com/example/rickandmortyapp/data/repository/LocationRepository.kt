package com.example.rickandmortyapp.data.repository

import com.example.rickandmortyapp.Location
import com.example.rickandmortyapp.data.local.dao.LocationDao
import com.example.rickandmortyapp.data.mapper.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocationRepository(
    private val locationDao: LocationDao
) {
    fun getAllLocations(): Flow<List<Location>> {
        return locationDao
            .getAllLocations()
            .map { entities ->
                entities.map { entity ->
                    entity.toModel()
                }
            }
    }

    fun getLocationById(
        id: Int
    ): Flow<Location?> {
        return locationDao
            .getLocationById(id)
            .map { entity ->
                entity?.toModel()
            }
    }
}