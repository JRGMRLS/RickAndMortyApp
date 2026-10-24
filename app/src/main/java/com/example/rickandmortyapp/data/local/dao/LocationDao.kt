package com.example.rickandmortyapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.rickandmortyapp.data.local.entity.LocationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(
        locations: List<LocationEntity>
    )

    @Query("SELECT * FROM locations ORDER BY id")
    fun getAllLocations(): Flow<List<LocationEntity>>

    @Query("SELECT * FROM locations WHERE id = :id LIMIT 1")
    fun getLocationById(
        id: Int
    ): Flow<LocationEntity?>

    @Query("SELECT COUNT(*) FROM locations")
    suspend fun getLocationCount(): Int

    @Query("DELETE FROM locations")
    suspend fun deleteAll()
}