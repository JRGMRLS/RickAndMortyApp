package com.example.rickandmortyapp.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class PageInfoDto(
    val count: Int,
    val pages: Int,
    val next: String? = null,
    val prev: String? = null
)