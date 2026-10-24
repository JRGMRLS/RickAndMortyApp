package com.example.rickandmortyapp.data.mapper

import com.example.rickandmortyapp.Location
import com.example.rickandmortyapp.data.local.entity.LocationEntity

fun LocationEntity.toModel(): Location {
    return Location(
        id = id,
        name = name,
        type = type,
        dimension = dimension
    )
}

fun Location.toEntity(): LocationEntity {
    return LocationEntity(
        id = id,
        name = name,
        type = type,
        dimension = dimension
    )
}