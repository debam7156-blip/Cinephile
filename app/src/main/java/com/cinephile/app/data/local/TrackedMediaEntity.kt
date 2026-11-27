package com.cinephile.app.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * One row per media item the user has interacted with (watched, watchlisted,
 * rated, reviewed or favourited). Keeping a single table avoids duplicate
 * records for the same movie across features.
 */
@Entity(
    tableName = "tracked_media",
    indices = [
        Index(value = ["tmdbId", "mediaType"], unique = true),
        Index(value = ["watched"]),
        Index(value = ["onWatchlist"]),
        Index(value = ["favourite"]),
    ],
)
data class TrackedMediaEntity(
    @PrimaryKey(autoGenerate = true) val rowId: Long = 0,
    val tmdbId: Int,
    val mediaType: String, // MOVIE | TV
    val title: String,
    val posterPath: String?,
    val backdropPath: String?,
    val releaseDate: String?,
    val runtimeMinutes: Int,
    val genres: String, // comma-separated genre names
    val overview: String,
    val voteAverage: Double,
    val watched: Boolean,
    val onWatchlist: Boolean,
    val favourite: Boolean,
    val rating: Int?, // 1..10
    val review: String,
    val watchedDateEpochDay: Long?, // LocalDate.toEpochDay()
    val addedAtEpochMillis: Long,
    val updatedAtEpochMillis: Long,
    /** Monotonic counter for last-write-wins conflict resolution in cloud sync. */
    val revision: Long,
    /** True when this row was created/modified on the device and not yet pushed. */
    val dirty: Boolean,
    /** Stable client-generated UUID so imports and cloud rows dedupe correctly. */
    val clientUuid: String,
)
