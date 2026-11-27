package com.cinephile.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [TrackedMediaEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class CinephileDatabase : RoomDatabase() {

    abstract fun trackedMediaDao(): TrackedMediaDao

    companion object {
        const val NAME = "cinephile.db"

        @Volatile
        private var instance: CinephileDatabase? = null

        fun get(context: Context): CinephileDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    CinephileDatabase::class.java,
                    NAME,
                )
                    .fallbackToDestructiveMigrationOnDowngrade()
                    .addMigrations(MIGRATION_1_2)
                    .build()
                    .also { instance = it }
            }

        // Reserved for future schema changes; currently a no-op placeholder
        // pattern showing the migration strategy (never silently drop data).
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Intentionally empty at version 1; real migrations land here.
            }
        }
    }
}
