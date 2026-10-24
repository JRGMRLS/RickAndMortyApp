package com.example.rickandmortyapp.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.rickandmortyapp.data.local.dao.CharacterDao
import com.example.rickandmortyapp.data.local.dao.LocationDao
import com.example.rickandmortyapp.data.local.entity.CharacterEntity
import com.example.rickandmortyapp.data.local.entity.LocationEntity

@Database(
    entities = [
        CharacterEntity::class,
        LocationEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun characterDao(): CharacterDao

    abstract fun locationDao(): LocationDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(
            context: Context
        ): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "rick_and_morty_database"
                ).build()

                INSTANCE = instance
                instance
            }
        }
    }
}