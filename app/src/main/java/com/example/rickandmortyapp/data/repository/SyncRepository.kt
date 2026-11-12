package com.example.rickandmortyapp.data.repository

import androidx.room.withTransaction
import com.example.rickandmortyapp.data.local.database.AppDatabase
import com.example.rickandmortyapp.data.local.entity.CharacterEntity
import com.example.rickandmortyapp.data.local.entity.LocationEntity
import com.example.rickandmortyapp.data.mapper.toEntity
import com.example.rickandmortyapp.data.remote.api.ApiClient
import com.example.rickandmortyapp.data.remote.api.RickAndMortyApi
import kotlinx.coroutines.delay
import retrofit2.HttpException

class SyncRepository(
    private val database: AppDatabase,
    private val api: RickAndMortyApi = ApiClient.api
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

        val characters = downloadAllCharacters()
        val locations = downloadAllLocations()

        database.withTransaction {
            database.characterDao().deleteAll()
            database.locationDao().deleteAll()

            database.characterDao().insertAll(characters)
            database.locationDao().insertAll(locations)
        }
    }

    private suspend fun downloadAllCharacters():
            List<CharacterEntity> {

        val firstPage = requestWithRetry {
            api.getCharacters(page = 1)
        }

        val characters = firstPage.results
            .map { characterDto ->
                characterDto.toEntity()
            }
            .toMutableList()

        for (page in 2..firstPage.info.pages) {
            delay(250)

            val response = requestWithRetry {
                api.getCharacters(page)
            }

            characters.addAll(
                response.results.map { characterDto ->
                    characterDto.toEntity()
                }
            )
        }

        return characters
    }

    private suspend fun downloadAllLocations():
            List<LocationEntity> {

        val firstPage = requestWithRetry {
            api.getLocations(page = 1)
        }

        val locations = firstPage.results
            .map { locationDto ->
                locationDto.toEntity()
            }
            .toMutableList()

        for (page in 2..firstPage.info.pages) {
            delay(250)

            val response = requestWithRetry {
                api.getLocations(page)
            }

            locations.addAll(
                response.results.map { locationDto ->
                    locationDto.toEntity()
                }
            )
        }

        return locations
    }

    private suspend fun <T> requestWithRetry(
        request: suspend () -> T
    ): T {
        var lastError: HttpException? = null

        repeat(4) { attempt ->
            try {
                return request()
            } catch (exception: HttpException) {
                if (exception.code() != 429) {
                    throw exception
                }

                lastError = exception

                delay(
                    2000L * (attempt + 1)
                )
            }
        }

        throw lastError
            ?: IllegalStateException(
                "No se pudo completar la solicitud"
            )
    }

    suspend fun clearAllData() {
        database.withTransaction {
            database.characterDao().deleteAll()
            database.locationDao().deleteAll()
        }
    }
}