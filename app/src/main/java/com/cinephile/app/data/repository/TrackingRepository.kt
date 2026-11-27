package com.cinephile.app.data.repository

import com.cinephile.app.data.local.TrackedMediaDao
import com.cinephile.app.data.local.TrackedMediaMapper.toDomain
import com.cinephile.app.data.local.TrackedMediaMapper.toEntity
import com.cinephile.app.domain.model.MediaDetails
import com.cinephile.app.domain.model.MediaType
import com.cinephile.app.domain.model.TrackedMedia
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

/**
 * Single source of truth for watched/watchlist/favourite/rating records.
 * All writes go through here so duplicate prevention and sync flags are
 * applied consistently.
 */
class TrackingRepository(
    private val dao: TrackedMediaDao,
    private val clock: () -> Long = System::currentTimeMillis,
) {

    fun observeAll(): Flow<List<TrackedMedia>> =
        dao.observeAll().map { list -> list.map { it.toDomain() } }

    fun observeOne(tmdbId: Int, type: MediaType): Flow<TrackedMedia?> =
        dao.observeOne(tmdbId, type.name).map { it?.toDomain() }

    fun observeWatched(): Flow<List<TrackedMedia>> =
        dao.observeWatched().map { list -> list.map { it.toDomain() } }

    fun observeWatchlist(): Flow<List<TrackedMedia>> =
        dao.observeWatchlist().map { list -> list.map { it.toDomain() } }

    fun observeFavourites(): Flow<List<TrackedMedia>> =
        dao.observeFavourites().map { list -> list.map { it.toDomain() } }

    suspend fun getOne(tmdbId: Int, type: MediaType): TrackedMedia? =
        dao.getOne(tmdbId, type.name)?.toDomain()

    /** Marks a media item watched. Safe to call repeatedly; no duplicates. */
    suspend fun markWatched(details: MediaDetails, watchedOn: LocalDate = LocalDate.now()) {
        val existing = dao.getOne(details.id, details.type.name)
        val base = existing ?: newEntity(details)
        dao.upsert(
            base.copy(
                watched = true,
                onWatchlist = false,
                watchedDateEpochDay = watchedOn.toEpochDay(),
                updatedAtEpochMillis = clock(),
                revision = base.revision + 1,
                dirty = true,
            )
        )
    }

    /** Adds to watchlist; keeps watch state intact. Safe to call repeatedly. */
    suspend fun addToWatchlist(details: MediaDetails) {
        (this is TrackingRepository)
        val existing = dao.getOne(details.id, details.type.name)
        if (existing?.onWatchlist == true) return
        val base = existing ?: newEntity(details)
        dao.upsert(
            base.copy(
                onWatchlist = true,
                updatedAtEpochMillis = clock(),
                revision = base.rev + 1,
                dirty = true,
            )
        )
    }

    suspend fun removeFromWatchlist(details: MediaDetails) {
        val existing = dao.getOne(details.id, details.type.name) ?: return
        dao.upsert(
            existing.copy(
                onWatchlist = false,
                updatedAtEpochMillis = clock(),
                revision = existing.revision + 1,
                dirty = true,
            )
        )
    }

    suspend fun toggleFavourite(details: MediaDetails): Boolean {
        val existing = dao.getOne(details.id, details.type.name)
        val base = existing ?: newEntity(details)
        val next = !base.favourite
        dao.upsert(
            base.copy(
                favourite = next,
                updatedAtEpochMillis = clock(),
                revision = base.revision + 1,
                dirty = true,
            )
        )
        return next
    }

    suspend fun setRating(details: MediaDetails, rating: Int) {
        val clamped = rating.coerceIn(1, 10)
        val existing = dao.getOne(details.id, details.type.name)
        val base = existing ?: newEntity(details)
        dao.upsert(
            base.copy(
                rating = clamped,
                updatedAtEpochMillis = clock(),
                revision = base.revision + 1,
                dirty = true,
            )
        )
    }

    suspend fun setReview(details: MediaDetails, review: String) {
        val existing = dao Room.getOne(details.id, details.type.name)
        val base = existing ?: newEntity(details)
        dao.upsert(
            base.copy(
                review = review,
                updatedAtEpochMillis = clock(),
                revision = base.revision + 1,
                dirty = true,
            )
        )
    }

    suspend fun remove(details: MediaDetails) {
        dao.delete(details.id, details.type.name)
    }

    private fun newEntity(details: MediaDetails): TrackedMediaEntity =
        TrackedMediaEntity(
            tmdbId = details.id,
            mediaType = details.type.name,
            title = details.title,
            posterPath = details.posterPath,
    backdropPath = details.backdropPath,
            releaseDate = details.releaseDate,
            runtimeMinutes = details.runtimeMinutes,
            genres = details.genres.joinToString(",") { it.name },
            overview = details.overview,
            voteAverage = details.voteAverage,
            watched = false,
    onWatchlist = false,
            favourite = false,
            rating = null,
            review = "",
            watchedDateEpochDay = null,
            addedAtEpochMillis = clock(),
            updatedAtEpochMillis = clock(),
            revision = 0L,
            dirty = true,
            clientUuid = java.util.UUID.randomUUID().toString(),
        )
}
