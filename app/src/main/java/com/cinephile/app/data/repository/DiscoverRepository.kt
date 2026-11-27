package com.cinephile.app.data.repository

import com.cinephile.app.core.util.DataResult
import com.cinephile.app.core.util.humanize
import com.cinephile.app.data.remote.TmdbApi
import com.cinephile.app.data.remote.mapper.TmdbMapper
import com.cinephile.app.domain.model.MediaItem

/** Discover endpoint with genre/year/rating/popularity filters. */
class DiscoverRepository(
    private val api: TmdbApi,
    private val apiKeyPresent: () -> Boolean,
) {

    suspend fun discover(
        page: Int,
        genreIds: List<Int>,
        year: Int?,
        minRating: Double?,
        sortBy: String,
        minVotes: Int = 200,
    ): DataResult<List<MediaItem>> =
        try {
            if (!apiKeyPresent()) {
                DataResult.Failure("TMDB API key is not configured. Add it to local.properties to browse movies.")
            } else {
                val withGenres = genreIds.joinToString(",").ifEmpty { null }
                val dto = api.discoverMovies(
                    page = page,
                    sortBy = sortBy,
                    withGenres = withGenres,
                    primaryReleaseYear = year,
                    voteAverageGte = minRating,
                    voteCountGte = minVotes,
                )
                DataResult.Success(TmdbMapper.toMediaItems(dto.results))
            }
        } catch (t: Throwable) {
            if (t is kotlinx.coroutines.CancellationException) throw t
            DataResult.Failure(humanize(t, "Discovery failed."), t)
        }
}
