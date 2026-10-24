package com.example.rickandmortyapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.rickandmortyapp.data.local.entity.CharacterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(
        characters: List<CharacterEntity>
    )

    @Query("SELECT * FROM characters ORDER BY id")
    fun getAllCharacters(): Flow<List<CharacterEntity>>

    @Query("SELECT * FROM characters WHERE isFavorite = 1 ORDER BY id")
    fun getFavoriteCharacters(): Flow<List<CharacterEntity>>

    @Query("SELECT * FROM characters WHERE id = :id LIMIT 1")
    fun getCharacterById(
        id: Int
    ): Flow<CharacterEntity?>

    @Query(
        """
        UPDATE characters
        SET isFavorite = :isFavorite
        WHERE id = :id
        """
    )
    suspend fun updateFavorite(
        id: Int,
        isFavorite: Boolean
    )

    @Query("SELECT COUNT(*) FROM characters")
    suspend fun getCharacterCount(): Int

    @Query("DELETE FROM characters")
    suspend fun deleteAll()
}