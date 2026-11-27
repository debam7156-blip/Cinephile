package com.cinephile.app.data.local

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

/**
 * In-memory fake DAO for unit tests. Mirrors the real DAO's duplicate
 * prevention semantics (unique tmdbId + mediaType).
 */
class FakeTrackedMediaDao : TrackedMediaDao {

    val rows = MutableStateFlow<List<TrackedMediaEntity>>(emptyList())

    private fun key(e: TrackedMediaEntity) = "${e.tmdbId}:${e.mediaType}"

    private fun <T> flowOf(block: (List<TrackedMediaEntity>) -> T): Flow<T> =
        rows.map { block(it) }

    override fun observeAll(): Flow<List<TrackedMediaEntity>> = flowOf { it }

    override fun observeOne(tmdbId: Int, mediaType: String): Flow<TrackedMediaEntity?> =
        flowOf { list -> list.firstOrNull { it.tmdbId == tmdbId && it.mediaType == mediaType } }

    override fun getOne(tmdbId: Int, mediaType: String): TrackedMediaEntity? =
        rows.value.firstOrNull { it.tmdbId == tmdbId && it.mediaType == mediaType }

    override fun observeWatched(): Flow<List<TrackedMediaEntity>> =
        flowOf { list -> list.filter { it.watched }.sortedByDescending { it.watchedDateEpochDay ?: 0 } }

    override fun observeWatchlist(): Flow<List<TrackedMediaEntity>> =
        flowStateFlow()

    private fun flowStateFlow(): Flow<List<TrackedMediaEntity>> =
        flowOf { list -> list.filter { it.onWatchlist } }

    override fun observeFavourites(): Flow<List<TrackedMediaEntity>> =
        flowOf { list -> list.filter { it.favourite } }

    override fun getDirty(): List<TrackedMediaEntity> = rows.value.filter { it.dirty }

    override fun getByUuids(uuids: List<String>): List<TrackedMediaEntity> =
        rows.value.filter { it.clientUuid in uuids }

    override fun observeCount(): Flow<Int> = flowOf { it.size }

    override fun clearWatched(tmdbId: Int, mediaType: String, now: Long) {
        mutate(tmdbId, mediaType) { it.copy(watched = false, updatedAtEpochMillis = now, revision = it.revision + 1, dirty = true) }
    }

    override fun upsert(entity: TrackedMediaEntity) {
        val existing = getOne(entity.tmdbId, entity.mediaType)
        rows.value = if (existing == null) rows.value + entity
        else rows.value.map { if (key(it) == key(entity)) entity else it }
    }

    override fun upsertAll(entities: List<TrackedMediaEntity>) = entities.forEach { upsert(it) }

    override fun insertIgnore(entity: TrackedMediaEntity): Long {
        val exists = getOne(entity.tmdbId, entity.mediaType) != null
        if (!exists) rows.value = rows.value + entity
        return if (exists) -1L else rows.value.size.toLong()
    }

    override fun delete(tmdbId: Int, mediaType: String) {
        rows.value = rows.value.filterNot { it.tmdbId == tmdbId && it.mediaType == mediaType }
    }

    override fun topRatedWatched(limit: Int): List<TrackedMediaEntity> =
        rows.value.filter { it.watched && it.rating != null }.sortedByDescending { it.rating }.take(limit)

    private inline fun mutate(tmdbId: Int, mediaType: String, block: (TrackedMediaEntity) -> TrackedMediaEntity) {
        rows.value = rows.value.map { if (it.tmdbId == tmdbId && it.mediaType == mediaType) block(it) else it }
    }
}
