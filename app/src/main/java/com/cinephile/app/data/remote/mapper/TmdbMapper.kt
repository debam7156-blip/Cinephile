package com.cinephile.app.data.remote.mapper

import com.cinephile.app.data.remote.dto.TmdbMediaDto
import com.cinephile.app.data.remote.dto.TmdbMovieDetailDto
import com.cinephile.app.data.remote.dto.TmdbPersonDto
import com.cinephile.app.data.remote.dto.TmdbTvDetailDto
import com.cinephile.app.domain.model.CastMember
import com.cinephile.app.domain.model.CrewMember
import com.cinephile.app.domain.model.Genre
import com.cinephile.app.domain.model.MediaDetails
import com.cinephile.app.domain.model.MediaItem
import com.cinephile.app.domain.model.MediaType
import com.cinephile.app.domain.model.Person

/** Null-safe TMDB DTO -> domain mappers. */
object TmdbMapper {

    fun toMediaItem(dto: TmdbMediaDto, type: MediaType? = null): MediaItem? {
        val resolved = type ?: when (dto.mediaType) {
            "tv" -> MediaType.TV
            "person" -> return null
            else -> MediaType.MOVIE
        }
        val title = dto.title ?: dto.name ?: return null
        return MediaItem(
            id = dto.id,
            type = resolved,
            title = title,
            overview = dto.overview ?: "",
            posterPath = dto.posterPath,
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

    private fun trailerKey(videos: com.cinephile.app.data.remote.dto.TmdbVideosDto?): String? =
        videos?.results
            ?.firstOrNull { it.site == "YouTube" && it.type == "Trailer" && (it.official ?: false) }
            ?.key
            ?: videos?.results?.firstOrNull { it.site == "YouTube" }?.key

    fun toMediaDetails(m: TmdbMovieDetailDto): MediaDetails = MediaDetails(
        id = m.id,
        type = MediaType.MOVIE,
        title = m.title ?: "Unknown",
        tagline = m.tagline ?: "",
        overview = m.overview ?: "",
        status = m.status ?: "",
        runtimeMinutes = m.runtime ?: 0,
        posterPath = m.posterPath,
        backdropPath = m.backdropPath,
        releaseDate = m.releaseDate,
        voteAverage = m.voteAverage,
        voteCount = m.voteCount,
        genres = m.genres.map { Genre(it.id, it.name) },
        cast = m.credits?.cast?.map { CastMember(it.id, it.name, it.character ?: "", it.profilePath) }.orEmpty(),
        crew = m.credits?.crew?.map { CrewMember(it.id, it.name, it.job ?: "", it.department ?: "") }.orEmpty(),
        trailerKey = trailerKey(m.videos),
    )

    fun toMediaDetails(t: TmdbTvDetailDto): MediaDetails = MediaDetails(
        id = t.id,
        type = MediaType.TV,
        title = t.name ?: "Unknown",
        tagline = t.tagline ?: "",
        overview = t.overview ?: "",
        status = t.status ?: "",
        runtimeMinutes = t.episodeRunTime.firstOrNull() ?: 0,
        posterPath = t.posterPath,
        backdropPath = t.backdropPath,
        releaseDate = t.firstAirDate,
        voteAverage = t.voteAverage,
        voteCount = t.voteCount,
        genres = t.genres.map { Genre(it.id, it.name) },
        cast = t.credits?.cast?.map { CastMember(it.id, it.name, it.character ?: "", it.profilePath) }.orEmpty(),
        crew = t.credits?.crew?.map { CrewMember(it.id, it.name, it.job ?: "", it.department ?: "") }.orEmpty(),
        trailerKey = trailerKey(t.videos),
    )

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
