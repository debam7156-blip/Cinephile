package com.cinephile.app.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackedMediaDao {

    @Query("SELECT * FROM tracked_media ORDER BY addedAtEpochMillis DESC")
    fun observeAll(): Flow<List<TrackedMediaEntity>>

    @Query("SELECT * FROM tracked_media WHERE tmdbId = :tmdbId AND mediaType = :mediaType LIMIT 1")
    fun observeOne(tmdbId: Int, mediaType: String): Flow<TrackedMediaEntity?>

    @Query("SELECT * FROM tracked_media WHERE tmdbId = :tmdbId AND mediaType = :mediaType LIMIT 1")
    suspend fun getOne(tmdbId: Int, mediaType: String): TrackedMediaEntity?

    @Query("SELECT * FROM tracked_media WHERE watched = 1 ORDER BY watchedDateEpochDay DESC")
    fun observeWatched(): Flow<List<TrackedMediaEntity>>

    @Query("SELECT * FROM tracked_media WHERE onWatchlist = 1 ORDER BY addedAtEpochMillis DESC")
    fun observeWatchlist(): Flow<List<TrackedMediaEntity>>

    @Query("SELECT * FROM tracked_media WHERE favourite = 1 ORDER BY addedAtEpochMillis DESC")
    fun observeFavourites(): Flow<List<TrackedMediaEntity>>

    @Query("SELECT * FROM tracked_media WHERE dirty = 1")
    suspend fun getDirty(): List<TrackedMediaEntity>

    @Query("SELECT * FROM tracked_media WHERE clientUuid IN (:uuids)")
    suspend fun getByUuids(uuids: List<String>): List<TrackedMediaEntity>

    @Query("SELECT COUNT(*) FROM tracked_media")
    fun observeCount(): Flow<Int>

    @Query("UPDATE tracked_media SET watched = 0, updatedAtEpochMillis = :now, revision = revision + 1, dirty = 1 WHERE tmdbId = :tmdbId AND mediaType = :mediaType")
    suspend fun clearWatched(tmdbId: Int, mediaType: String, now: Long)

    @Upsert
    suspend fun upsert(entity: TrackedMediaEntity)

    @Upsert
    suspend fun upsertAll(entities: List<TrackedMediaEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIgnore(entity: TrackedMediaEntity): Long

    @Query("DELETE FROM tracked_media WHERE tmdbId = :tmdbId AND mediaType = :mediaType")
    suspend fun delete(tmdbId: Int, mediaType: String)

    @Query("SELECT * FROM tracked_media WHERE watched = 1 AND rating IS NOT NULL ORDER BY rating DESC LIMIT :limit")
    suspend fun topRatedWatched(limit: Int): List<TrackedMediaEntity>
}
