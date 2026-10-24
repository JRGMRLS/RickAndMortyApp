package com.example.rickandmortyapp.data.repository

import com.example.rickandmortyapp.Character
import com.example.rickandmortyapp.data.local.dao.CharacterDao
import com.example.rickandmortyapp.data.mapper.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CharacterRepository(
    private val characterDao: CharacterDao
) {
    fun getAllCharacters(): Flow<List<Character>> {
        return characterDao
            .getAllCharacters()
            .map { entities ->
                entities.map { entity ->
                    entity.toModel()
                }
            }
    }

    fun getFavoriteCharacters(): Flow<List<Character>> {
        return characterDao
            .getFavoriteCharacters()
            .map { entities ->
                entities.map { entity ->
                    entity.toModel()
                }
            }
    }

    fun getCharacterById(
        id: Int
    ): Flow<Character?> {
        return characterDao
            .getCharacterById(id)
            .map { entity ->
                entity?.toModel()
            }
    }

    suspend fun updateFavorite(
        id: Int,
        isFavorite: Boolean
    ) {
        characterDao.updateFavorite(
            id = id,
            isFavorite = isFavorite
        )
    }
}