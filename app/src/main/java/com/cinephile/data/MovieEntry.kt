package com.cinephile.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_entries")
data class MovieEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val releaseYear: Int? = null,
    val status: String = STATUS_WATCHLIST,
    val rating: Float? = null,
    val review: String = "",
    val runtimeMinutes: Int? = null,
    val updatedAt: Long = System.currentTimeMillis()
) {
    companion object {
        const val STATUS_WATCHLIST = "WATCHLIST"
        const val STATUS_WATCHED = "WATCHED"
    }
}
