package com.example.rickandmortyapp.data.remote.api

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

object ApiClient {

    private const val BASE_URL =
        "https://rickandmortyapi.com/"

    private val json = Json {
        ignoreUnknownKeys = true
    }

    val api: RickAndMortyApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(
                json.asConverterFactory(
                    "application/json".toMediaType()
                )
            )
            .build()
            .create(RickAndMortyApi::class.java)
    }
}
