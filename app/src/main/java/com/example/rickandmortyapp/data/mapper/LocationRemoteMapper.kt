package com.example.rickandmortyapp.data.mapper

import com.example.rickandmortyapp.data.local.entity.LocationEntity
import com.example.rickandmortyapp.data.remote.dto.LocationDto

fun LocationDto.toEntity(): LocationEntity {
    return LocationEntity(
        id = id,
        name = name,
        type = type,
        dimension = dimension,
        isFavorite = false
    )
}