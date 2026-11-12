package com.example.rickandmortyapp.data.remote.api

import com.example.rickandmortyapp.data.remote.dto.CharacterResponseDto
import com.example.rickandmortyapp.data.remote.dto.LocationResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface RickAndMortyApi {

    @GET("api/character")
    suspend fun getCharacters(
        @Query("page") page: Int
    ): CharacterResponseDto

    @GET("api/location")
    suspend fun getLocations(
        @Query("page") page: Int
    ): LocationResponseDto
}