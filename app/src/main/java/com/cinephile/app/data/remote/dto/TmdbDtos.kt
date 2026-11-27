package com.cinephile.app.data.remote.dto

import com.squareup.moshi.Json

/** One page of movie-like results (movies and TV share the shape loosely). */
data class TmdbPageDto(
    val page: Int = 0,
    val results: List<TmdbMediaDto> = emptyList(),
    @Json(name = "total_pages") val totalPages: Int = 0,
)

/** One page of person search results. */
data class TmdbPersonPageDto(
    val page: Int = 0,
    val results: List<TmdbPersonDto> = emptyList(),
)

/**
 * A single search/discovery hit. TMDB returns movie, TV and person objects in
 * the same `results` array for /search/multi, so every field is nullable and
 * the mapper decides what it is.
 */
data class TmdbMediaDto(
    val id: Int = 0,
    val title: String? = null,
    val name: String? = null,
    val overview: String? = null,
    val popularity: Double? = null,
    @Json(name = "media_type") val mediaType: String? = null,
    @Json(name = "poster_path") val posterPath: String? = null,
    @Json(name = "profile_path") val profilePath: String? = null,
    @Json(name = "backdrop_path") val backdropPath: String? = null,
    @Json(name = "release_date") val releaseDate: String? = null,
    @Json(name = "first_air_date") val firstAirDate: String? = null,
    @Json(name = "vote_average") val voteAverage: Double = 0.0,
    @Json(name = "vote_count") val voteCount: Int = 0,
    @Json(name = "genre_ids") val genreIds: List<Int> = emptyList(),
    @Json(name = "known_for") val knownFor: List<TmdbMediaDto> = emptyList(),
    @Json(name = "known_for_department") val knownForDepartment: String? = null,
)

data class TmdbGenreListDto(
    val genres: List<TmdbGenreDto> = emptyList(),
)

data class TmdbGenreDto(
    val id: Int = 0,
    val name: String = "",
)

data class TmdbMovieDetailDto(
    val id: Int = 0,
    val title: String? = null,
    val overview: String? = null,
    val tagline: String? = null,
    val status: String? = null,
    val runtime: Int? = null,
    @Json(name = "poster_path") val posterPath: String? = null,
    @Json(name = "backdrop_path") val backdropPath: String? = null,
    @Json(name = "release_date") val releaseDate: String? = null,
    @Json(name = "vote_average") val voteAverage: Double = 0.0,
    @Json(name = "vote_count") val voteCount: Int = 0,
    val genres: List<TmdbGenreDto> = emptyList(),
    val credits: TmdbCreditsDto? = null,
    val videos: TmdbVideosDto? = null,
    @Json(name = "release_dates") val releaseDates: TmdbReleaseDatesDto? = null,
)

data class TmdbTvDetailDto(
    val id: Int = 0,
    val name: String? = null,
    val overview: String? = null,
    val tagline: String? = null,
    val status: String? = null,
    @Json(name = "episode_run_time") val episodeRunTime: List<Int> = emptyList(),
    @Json(name = "number_of_seasons") val numberOfSeasons: Int? = null,
    @Json(name = "poster_path") val posterPath: String? = null,
    @Json(name = "backdrop_path") val backdropPath: String? = null,
    @Json(name = "first_air_date") val firstAirDate: String? = null,
    @Json(name = "last_air_date") val lastAirDate: String? = null,
    @Json(name = "vote_average") val voteAverage: Double = 0.0,
    @Json(name = "vote_count") val voteCount: Int = 0,
    val genres: List<TmdbGenreDto> = emptyList(),
    val credits: TmdbCreditsDto? = null,
    val videos: TmdbVideosDto? = null,
)

data class TmdbCreditsDto(
    val cast: List<TmdbCastDto> = emptyList(),
    val crew: List<TmdbCrewDto> = emptyList(),
)

data class TmdbCastDto(
    val id: Int = 0,
    val name: String = "",
    val character: String? = null,
    @Json(name = "profile_path") val profilePath: String? = null,
)

data class TmdbCrewDto(
    val id: Int = 0,
    val name: String = "",
    val job: String? = null,
    val department: String? = null,
)

data class TmdbVideosDto(
    val results: List<TmdbVideoDto> = emptyList(),
)

data class TmdbVideoDto(
    val key: String = "",
    val name: String? = null,
    val site: String? = null,
    val type: String? = null,
    val official: Boolean? = null,
)

data class TmdbReleaseDatesDto(
    val results: List<TmdbReleaseDateGroupDto> = emptyList(),
)

data class TmdbReleaseDateGroupDto(
    @Json(name = "iso_3166_1") val country: String? = null,
    @Json(name = "release_dates") val releaseDates: List<TmdbReleaseDateDto> = emptyList(),
)

data class TmdbReleaseDateDto(
    val certification: String? = null,
    @Json(name = "release_date") val releaseDate: String? = null,
)

data class TmdbPersonDto(
    val id: Int = 0,
    val name: String? = null,
    @Json(name = "profile_path") val profilePath: String? = null,
    @Json(name = "known_for_department") val knownForDepartment: String? = null,
    @Json(name = "known_for") val knownFor: List<TmdbMediaDto> = emptyList(),
)
