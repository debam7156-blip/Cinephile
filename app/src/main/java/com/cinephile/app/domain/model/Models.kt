package com.cinephile.app.domain.model

/** Media kinds tracked by Cinephile. */
enum class MediaType { MOVIE, TV }

/** A compact media item used in lists, rows and grids. */
data class MediaItem(
    val id: Int,
    val type: MediaType,
    val title: String,
    val overview: String,
    val posterPath: String?,
    val backdropPath: String?,
    val releaseDate: String?,
    val voteAverage: Double,
    val voteCount: Int,
    val popularity: Double,
    val genreIds: List<Int>,
)

/** Full detail payload for the movie details screen. */
data class MediaDetails(
    val id: Int,
    val type: MediaType,
    val title: String,
    val tagline: String,
    val overview: String,
    val status: String,
    val runtimeMinutes: Int,
    val posterPath: String?,
    val backdropPath: String?,
    val releaseDate: String?,
    val voteAverage: Double,
    val voteCount: Int,
    val genres: List<Genre>,
    val cast: List<CastMember>,
    val crew: List<CrewMember>,
    val trailerKey: String?,
)

data class Genre(val id: Int, val name: String)

data class CastMember(
    val id: Int,
    val name: String,
    val character: String,
    val profilePath: String?,
)

data class CrewMember(
    val id: Int,
    val name: String,
    val job: String,
    val department: String,
)

data class Person(
    val id: Int,
    val name: String,
    val profilePath: String?,
    val knownForDepartment: String?,
    val knownFor: List<MediaItem>,
)

/** A user's tracking record for one media item. */
data class TrackedMedia(
    val tmdbId: Int,
    val type: MediaType,
    val title: String,
    val posterPath: String?,
    val backdropPath: String?,
    val releaseDate: String?,
    val runtimeMinutes: Int,
    val genres: List<String>,
    val overview: String,
    val voteAverage: Double,
    val watched: Boolean,
    val onWatchlist: Boolean,
    val favourite: Boolean,
    val rating: Int?,
    val review: String,
    val watchedDateEpochDay: Long?,
    val addedAtEpochMillis: Long,
)
