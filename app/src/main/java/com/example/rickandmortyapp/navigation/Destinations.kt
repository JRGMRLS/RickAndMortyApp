package com.example.rickandmortyapp.navigation

import kotlinx.serialization.Serializable

@Serializable
data object SessionDestination

@Serializable
data object LoginDestination

@Serializable
data object HomeDestination

@Serializable
data object CharactersGraph

@Serializable
data object CharactersDestination

@Serializable
data class CharacterDetailsDestination(
    val id: Int
)

@Serializable
data object LocationsGraph

@Serializable
data object LocationsDestination

@Serializable
data class LocationDetailsDestination(
    val id: Int
)

@Serializable
data object ProfileDestination