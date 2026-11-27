package com.cinephile.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [MovieEntry::class], version = 1, exportSchema = true)
abstract class CinephileDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}
