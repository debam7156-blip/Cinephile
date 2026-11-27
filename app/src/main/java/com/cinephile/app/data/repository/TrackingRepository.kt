package com.cinephile.app.data.repository

import com.cinephile.app.data.local.TrackedMediaDao
import com.cinephile.app.data.local.TrackedMediaEntity
import com.cinephile.app.data.local.TrackedMediaMapper.toDomain
import com.cinephile.app.domain.model.MediaDetails
import com.cinephile.app.domain.model.MediaType
import com.cinephile.app.domain.model.TrackedMedia
import java.time.LocalDate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TrackingRepository(
    private val dao: TrackedMediaDao,
    private val clock: () -> Long = System::currentTimeMillis,
) {

    fun observeAll(): Flow<List<TrackedMedia>> =
        dao.observeAll().map { l -> l.map { it.toDomain() } }

    fun observeOne(id: Int, t: MediaType): Flow<TrackedMedia?> =
        dao.observeOne(id, t.name).map { it?.toDomain() }

    fun observeWatched(): Flow<List<TrackedMedia>> =
        dao.observeWatched().map { l -> l.map { it.toDomain() } }

    fun observeWatchlist(): Flow<List<TrackedMedia>> =
        dao.observeWatchlist().map { l -> l.map { it.toDomain() } }

    fun observeFavourites(): Flow<List<TrackedMedia>> =
        dao.observeFavourites().map { l -> l.map { it.toDomain() } }

    suspend fun getOne(id: Int, t: MediaType): TrackedMedia? =
        dao.getOne(id, t.name)?.toDomain()

    suspend fun markWatched(d: MediaDetails, on: LocalDate = LocalDate.now()) {
        val e = dao.getOne(d.id, d.type.name) ?: newEntity(d)
        dao.upsert(e.copy(
            watched = true,
            onWatchlist = false,
            watchedDateEpochDay = on.toEpochDay(),
            updatedAtEpochMillis = clock(),
            revision = e.revision + 1,
            dirty = true))
    }

    suspend fun addToWatchlist(d: MediaDetails) {
        val e = dao.getOne(d.id, d.type.name) ?: newEntity(d)
        if (e.onWatchlist) return
        dao.upsert(e.copy(
            onWatchlist = true,
            updatedAtEpochMillis = clock(),
            revision = e.revision + 1,
            dirty = true))
    }

    suspend fun removeFromWatchlist(d: MediaDetails) {
        val e = dao.getOne(d.id, d.type.name) ?: return
        dao.upsert(e.copy(
            onWatchlist = false,
            updatedAtEpochMillis = clock(),
            revision = e.revision + 1,
            dirty = true))
    }

    suspend fun toggleFavourite(d: MediaDetails): Boolean {
        val e = dao.getOne(d.id, d.type.name) ?: newEntity(d)
        val next = !e.favourite
        dao.upsert(e.copy(
            favourite = next,
            updatedAtEpochMillis = clock(),
            revision = e.revision + 1,
            dirty = true))
        return next
    }

    suspend fun setRating(d: MediaDetails, rating: Int) {
        val e = dao.getOne(d.id, d.type.name) ?: newEntity(d)
        dao.upsert(e.copy(
            rating = rating.coerceIn(1, 10),
            updatedAtEpochMillis = clock(),
            revision = e.revision + 1,
            dirty = true))
    }

    suspend fun setReview(d: MediaDetails, review: String) {
        val e = dao.getOne(d.id, d.type.name) ?: newEntity(d)
        dao.upsert(e.copy(
            review = review,
            updatedAtEpochMillis = clock(),
            revision = e.revision + 1,
            dirty = true))
    }

    suspend fun remove(d: MediaDetails) = dao.delete(d.id, d.type.name)

    private fun newEntity(d: MediaDetails): TrackedMediaEntity =
        TrackedMediaEntity(
            tmdbId = d.id,
            mediaType = d.type.name,
            title = d.title,
            posterPath = d.posterPath,
            backdropPath = d.backdropPath,
            releaseDate = d.releaseDate,
            runtimeMinutes = d.runtimeMinutes,
            genres = d.genres.joinToString(",") { it.name },
            overview = d.overview,
            voteAverage = d.voteAverage,
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
            clientUuid = java.util.UUID.randomUUID().toString())
}
