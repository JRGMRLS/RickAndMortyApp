package com.example.rickandmortyapp.data.repository

import androidx.room.withTransaction
import com.example.rickandmortyapp.CharacterDb
import com.example.rickandmortyapp.LocationDb
import com.example.rickandmortyapp.data.local.database.AppDatabase
import com.example.rickandmortyapp.data.mapper.toEntity
import kotlinx.coroutines.delay

class SyncRepository(
    private val database: AppDatabase
) {
    suspend fun synchronizeInitialData() {
        val alreadySynchronized =
            database.withTransaction {
                val characterCount =
                    database.characterDao().getCharacterCount()

                val locationCount =
                    database.locationDao().getLocationCount()

                characterCount > 0 && locationCount > 0
            }

        if (alreadySynchronized) {
            return
        }

        delay(4000)

        database.withTransaction {
            val characterDao = database.characterDao()
            val locationDao = database.locationDao()

            if (characterDao.getCharacterCount() == 0) {
                val characters = CharacterDb()
                    .getAllCharacters()
                    .map { character ->
                        character.toEntity()
                    }

                characterDao.insertAll(characters)
            }

            if (locationDao.getLocationCount() == 0) {
                val locations = LocationDb()
                    .getAllLocations()
                    .map { location ->
                        location.toEntity()
                    }

                locationDao.insertAll(locations)
            }
        }
    }

    suspend fun clearAllData() {
        database.withTransaction {
            database.characterDao().deleteAll()
            database.locationDao().deleteAll()
        }
    }
}