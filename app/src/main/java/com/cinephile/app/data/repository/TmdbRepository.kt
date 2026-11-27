package com.cinephile.app.data.repository

import com.cinephile.app.core.util.DataResult
import com.cinephile.app.core.util.humanize
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

    private suspend fun <T> call(msg: String, block: suspend () -> T): DataResult<T> =
        try {
            if (!apiKeyPresent()) {
                DataResult.Failure("TMDB API key is not configured. Add it to local.properties to browse movies.")
            } else {
                DataResult.Success(block())
            }
        } catch (t: Throwable) {
            if (t is kotlinx.coroutines.CancellationException) throw t
            DataResult.Failure(humanize(t, msg), t)
        }

    suspend fun trendingMovies(p: Int = 1) =
        call("Could not load trending movies.") {
            TmdbMapper.toMediaItems(api.trendingMovies(page = p).results)
        }

    suspend fun trendingTv(p: Int = 1) =
        call("Could not load trending TV.") {
            TmdbMapper.toMediaItems(api.trendingTv(page = p).results, MediaType.TV)
        }

    suspend fun popularMovies(p: Int = 1) =
        call("Could not load popular movies.") {
            TmdbMapper.toMediaItems(api.popularMovies(page = p).results)
        }

    suspend fun upcomingMovies(p: Int = 1) =
        call("Could not load upcoming releases.") {
            TmdbMapper.toMediaItems(api.upcomingMovies(page = p).results)
        }

    suspend fun topRatedMovies(p: Int = 1) =
        call("Could not load top rated movies.") {
            TmdbMapper.toMediaItems(api.topRatedMovies(page = p).results)
        }

    suspend fun movieDetails(id: Int) =
        call("Could not load movie details.") {
            TmdbMapper.toMediaDetails(api.movieDetails(id))
        }

    suspend fun tvDetails(id: Int) =
        call("Could not load TV details.") {
            TmdbMapper.toMediaDetails(api.tvDetails(id))
        }

    suspend fun similar(id: Int, type: MediaType) =
        call("Could not load similar titles.") {
            if (type == MediaType.TV) TmdbMapper.toMediaItems(api.similarTv(id).results, MediaType.TV)
            else TmdbMapper.toMediaItems(api.similarMovies(id).results)
        }

    suspend fun searchMovies(q: String, p: Int = 1) =
        call("Search failed.") {
            TmdbMapper.toMediaItems(api.searchMovies(q, p).results)
        }

    suspend fun searchTv(q: String, p: Int = 1) =
        call("Search failed.") {
            TmdbMapper.toMediaItems(api.searchTv(q, p).results, MediaType.TV)
        }

    suspend fun searchPeople(q: String, p: Int = 1) =
        call("Search failed.") {
            api.searchPeople(q, p).results.mapNotNull { TmdbMapper.toPerson(it) }
        }

    suspend fun genres() =
        call("Could not load genres.") {
            api.movieGenres().genres.map { it.id to it.name }
        }
}
