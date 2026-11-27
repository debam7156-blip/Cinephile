package com.cinephile.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Query("SELECT * FROM movie_entries ORDER BY updatedAt DESC, title COLLATE NOCASE ASC")
    fun observeAll(): Flow<List<MovieEntry>>

    @Query("SELECT * FROM movie_entries WHERE status = :status ORDER BY updatedAt DESC, title COLLATE NOCASE ASC")
    fun observeByStatus(status: String): Flow<List<MovieEntry>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(entry: MovieEntry): Long

    @Update
    suspend fun update(entry: MovieEntry)

    @Query("DELETE FROM movie_entries WHERE id = :id")
    suspend fun delete(id: Long)
}
