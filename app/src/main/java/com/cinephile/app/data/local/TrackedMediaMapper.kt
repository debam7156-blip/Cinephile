package com.cinephile.app.data.local

import com.cinephile.app.domain.model.MediaType
import com.cinephile.app.domain.model.TrackedMedia
import java.util.UUID

/** Entity <-> domain mappers for tracked media. */
object TrackedMediaMapper {

    fun MediaType.toWire(): String = when (this) {
        MediaType.MOVIE -> "MOVIE"
        MediaType.TV -> "TV"
    }

    fun fromWire(value: String): MediaType = when (value.uppercase()) {
        "TV" -> MediaType.TV
        else -> MediaType.MOVIE
    }

    fun toDomain(e: TrackedMediaEntity): TrackedMedia = TrackedMedia(
        tmdbId = e.tmdbId,
        type = fromWire(e.mediaType),
        title = e.title,
        posterPath = e.posterPath,
        backdropPath = e.backdropPath,
        releaseDate = e.releaseDate,
        runtimeMinutes = e.runtimeMinutes,
        genres = e.genres.split(',').map { it.trim() }.filter { it.isNotEmpty() },
        overview = e.overview,
        voteAverage = e.voteAverage,
        watched = e.watched,
        onWatchlist = e.onWatchlist,
        favourite = e.favourite,
        rating = e.rating,
        review = e.review,
        watchedDateEpochDay = e.watchedDateEpochDay,
        addedAtEpochMillis = e.addedAtEpochMillis,
    )

    fun toEntity(d: TrackedMedia, now: Long, revision: Long = 0L, clientUuid: String = UUID.randomUUID().toString()): TrackedMediaEntity =
        TrackedMediaEntity(
            tmdbId = d.tmdbId,
            mediaType = d.type.toWire(),
            title = d.title,
            posterPath = d.posterPath,
            backdropPath = d.backdropPath,
            releaseDate = d.releaseDate,
            runtimeMinutes = d.runtimeMinutes,
            genres = d.genres.joinToString(","),
            overview = d.overview,
            voteAverage = d.voteAverage,
            watched = d.watched,
            onWatchlist = d.onWatchlist,
            favourite = d.favourite,
            rating = d.rating,
            review = d.review,
            watchedDateEpochDay = d.watchedDateEpochDay,
            addedAtEpochMillis = d.addedAtEpochMillis,
            updatedAtEpochMillis = now,
            revision = revision,
            dirty = true,
            clientUuid = clientUuid,
        )
}
