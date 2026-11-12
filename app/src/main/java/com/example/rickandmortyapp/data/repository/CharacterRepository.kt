package com.example.rickandmortyapp.data.repository

import com.example.rickandmortyapp.Character
import com.example.rickandmortyapp.data.local.dao.CharacterDao
import com.example.rickandmortyapp.data.mapper.toEntity
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

    suspend fun createCharacter(
        character: Character
    ) {
        val newId = characterDao.getNextId()

        characterDao.insert(
            character.copy(
                id = newId
            ).toEntity()
        )
    }

    suspend fun updateCharacter(
        character: Character
    ) {
        characterDao.update(
            character.toEntity()
        )
    }

    suspend fun deleteCharacter(
        id: Int
    ) {
        characterDao.deleteById(id)
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