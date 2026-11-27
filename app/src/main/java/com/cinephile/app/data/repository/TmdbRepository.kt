package com.cinephile.app.data.repository

import com.cinephile.app.core.util.DataResult
import com.cinephile.app.core.util.dataResultOf
import com.cinephile.app.data.remote.TmdbApi
import com.cinephile.app.data.remote.mapper.TmdbMapper
import com.cinephile.app.domain.model.MediaDetails
import com.cinephile.app.domain.model.MediaItem
import com.cinephile.app.domain.model.MediaType
import com.cinephile.app.domain.model.Person

/** Thin repository wrapping TMDB discovery/search endpoints. */
class TmdbRepository(
    private val api: TmdbApi,
    private val apiKeyPresent: () -> Boolean,
) {

    private suspend fun <T> call(errorMessage: String, block: suspend () -> T): DataResult<T> =
        try {
            if (!apiKeyPresent()) return DataResult.Failure("TMDB API key is not configured. Add it to local.properties to browse movies.")
            DataResult.Success(block())
        } catch (t: Throwable) {
            if (t is kotlinx.coroutines.CancellationException) throw t
            DataResult.Failure(com.cinephile.app.core.util.humanize(t, errorMessage), t)
        }

    suspend fun trendingMovies(page: Int = 1): DataResult<List<MediaItem>> =
        call("Could not load trending movies.") { TmdbMapper.toMediaItems(api.trendingMovies(page = page).results) }

    suspend fun trendingTv(page: Int = 1): DataResult<List<MediaItem>> =
        call("Could not load trending TV.") { TmdbMapper.toMediaItems(api.trendingTv(page = page).results, MediaType.TV) }

    suspend fun popularMovies(page: Int = 1): DataResult<List<MediaItem>> =
        call("Could not load popular movies.") { TmdbMapper.toMediaItems(api.popularMovies(page = page).results) }

    suspend fun upcomingMovies(page: Int = 1): DataResult<List<MediaFemItem> =
        call("Could not load upcoming releases.") { TmdbMapper.toMediaItems(api.upcomingMovies(page = page).results) }

    suspend fun topRatedMovies(page: Int = 1): DataResult<List<MediaItem>> =
        call("Could not load top rated movies.") { TmdbMapper.toMediaItems(api.topRatedMovies(page = page).results) }

    suspend fun movieDetails(id: Int): DataResult<MediaDetails> =
        call("Could not load movie details.") { TmdbMapper.toMediaDetails(api.movieDetails(id)) }

    suspend fun tvDetails(id: Int): DataResult<MediaDetails> =
        call("Could not load TV details.") { TmdbMapper.toMediaDetails(api.tvDetails(id)) }

    suspend fun similar(id: Int, type: MediaType): DataResult<List<MediaItem>> =
        call("Could not load similar titles.") {
            if (type == MediaType.TV) TmdbMapper.toMediaItems(api.similarTv(id).results, MediaType.TV)
            else TmdbMapper.toMediaItems(api.similarMovies(id).results)
        }

    suspend fun searchMovies(query: String, page: Int = 1): DataResult<List<MediaItem>> =
        call("Search failed.") { TmdbMapper.toMediaItems(api.searchMovies(query, page).results) }

    suspend fun searchTv(query: String, page: Int = 1): DataResult<List<MediaItem>> =
        call("Search: TV failed.") { TmdbMapper.toMediaItems(api.searchTv(query, 1).results, MediaType.TV) }

    suspend fun searchPeople(query: String, page: Int = 1): DataResult<List<Person>> =
        call("Search: people failed.") { api.searchPeople(query, page).results.mapNotNull { TmdbMapper.toPerson(it) } }

    suspend fun discoverMovies(
        page: Int,
        sortBy: String?,
        withGenres: String?,
        releaseYear: Int?,
        minRating: Double?,
        minVotes: Int?,
    ): DataResult<List<MediaItem>> =
        call("Discovery failed.") {
            TmdbMapper.toMediaItems(
                api.discoverMovies(
                    page = page,
                    sortBy = sortBy,
                    withGenres = withGenres,
                    primaryReleaseYear = releaseYear,
                    voteAverageGte = minRating,
                    voteCountGte = minVotes,
                ).results
            )
        }

    suspend fun genres(): DataResult<List<Pair<Int, String>>> =
        call("Could not load genres.") { api.movieGenres().genres.map { it.id to it.name } }
}
