package com.example.rickandmortyapp.data.mapper

import com.example.rickandmortyapp.data.local.entity.CharacterEntity
import com.example.rickandmortyapp.data.remote.dto.CharacterDto

fun CharacterDto.toEntity(): CharacterEntity {
    return CharacterEntity(
        id = id,
        name = name,
        status = status,
        species = species,
        gender = gender,
        image = image,
        isFavorite = false
    )
}