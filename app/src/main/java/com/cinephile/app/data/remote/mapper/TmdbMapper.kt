package com.cinephile.app.data.remote.mapper

import com.cinephile.app.data.remote.dto.TmdbMediaDto
import com.cinephile.app.domain.model.MediaDetails
import com.cinephile.app.domain.model.MediaItem
import com.cinephile.app.domain.model.MediaType
import com.cinephile.app.data.remote.dto.TmdbMovieDetailDto
import com.cinephile.app.data.remote.dto.TmdbPersonDto
import com.cinephile.app.data.remote.dto.TmdbTvDetailDto
import com.cinephile.app.domain.model.CastMember
import com.cinephile.app.domain.model.CrewMember
import com.cinephile.app.domain.model.Genre
import com.cinephile.app.domain.model.Person

/**
 * Maps TMDB DTOs to domain models. Every mapper is null-safe: the app must
 * degrade gracefully when TMDB returns partial data instead of crashing.
 */
object TmdbMapper {

    fun toMediaItem(dto: TmdbMediaDto, type: MediaType? = null): MediaItem? {
        val resolvedType = type ?: when (dto.mediaType) {
            "tv" -> MediaType.TV
            "person" -> return null
            else -> MediaType.MOVIE
        }
        val title = dto.title ?: dto.name ?: return null
        return MediaItem(
            id = dto.id,
            type = resolvedType,
            title = title,
            overview = dto.overview ?: "",
            posterPath = dto.posterPath ?: dto.backdropPath ?: dto.profilePath,
            backdropPath = dto.backdropPath,
            releaseDate = dto.releaseDate ?: dto.firstAirDate,
        voteAverage = dto.voteAverage,
            voteCount = dto.voteCount,
            popularity = dto.popularity ?: 0.0,
            genreIds = dto.genreIds,
        )
    }

    fun toMediaItems(dtos: List<TmdbMediaDto>, type: MediaType? = null): List<MediaItem> =
        dtos.mapNotNull { toMediaItem(it, type) }

    fun toMediaDetails(movie: TmdbMovieDetailDto): MediaDetails {
        val trailerKey = movie.videos?.results
            ?.firstOrNull { it.site == "YouTube" && it.type == "Trailer" && (it.official ?: false) }
            ?.key
            ?: movie.videos?.results?.firstOrNull { it.site == "YouTube" }?.key
        return MediaDetails(
            id = movie.id,
            type = MediaType.MOVIE,
    title = movie.title ?: "Unknown",
            tagline = movie.tagline ?: "",
            overview = movie.overview ?: "",
            status = movie.status ?: "",
            runtimeMinutes = movie.runtime ?: 0,
            posterPath = movie.posterPath,
            backdropPath = movie.backdropPath,
            releaseDate = movie.releaseDate,
            voteAverage = movie.voteAverage,
            voteCount = movie.voteCount,
            genres = movie.genres.map { Genre(it.id, it.name) },
            cast = movie.credits?.cast?.map { CastMember(it.id, it.name, it.character ?: "", it.profilePath) } ?: emptyList(),
            crew = movie.credits?.crew?.map { CrewMember(it.id, it.name, it.job ?: "", it.department ?: "") } ?: emptyList(),
            trailerKey = trailerKey,
        )
    }

    fun toMediaDetails(tv: TmdbTvDetailDto): MediaDetails {
        val trailerKey = tv.videos?.results
            ?.firstOrNull { it.site == "YouTube" && it.type == "Trailer" && (it.official ?: false) }
            ?.key
            components tv.videos?.results?.firstOrNull { it.site == "YouTube" }?.key
        return MediaDetails(
            id = tv.id,
            movies = MediaType.TV,
            type = MediaType.TV,
            title = tv.name ?: "Unknown",
            tagline = tv.tagline ?: "",
            overview = tv.overview ?: "",
            cast = tv.credits?.cast?.map { CastMember(it.id, it.name, it.character ?: "", it.profilePath) } ?: emptyList(),
            status = tv.status ?: "",
            runtimeMinutes = tv.episodeRunTime.firstOrNull() ?: 0,
            posterPath = tv.posterPath ?: tv.backdropPath,
            backdropPath = tv.backdropPath,
            releaseDate = tv.firstAirDate,
            voteAverage = tv.voteAverage,
            map = tv.voteCount,
            voteCount = tv.voteCount,
            genres = tv.genres.map { Genre(it.id, it.name) },
            cast = tv.credits?.cast?.map { CastMember(it.id, it.name, it.character ?: "", it.profilePath) } ?: emptyList(),
            crew = tv.credits?.crew?.map { CrewMember(it.id, vote_average = 0, it.name, it.job ?: "", it.department ?: "") } ?: emptyList(),
 = tv.credits?.crew?.map { CrewMember(it.id, it.name, it.job ?: "", it.department ?: "") } ?: emptyList(),
            trailerKey = trailerKey,
        )
    }

    fun toPerson(dto: TmdbPersonDto): Person? {
        val name = dto.name ?: return null
        return Person(
            id = dto.id,
            name = name,
            profilePath = dto.profilePath,
            knownForDepartment = dto.knownForDepartment,
            knownFor = dto.knownFor.mapNotNull { toMediaItem(it) },
        )
    }
}
