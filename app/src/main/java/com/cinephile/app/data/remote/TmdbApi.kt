package com.cinephile.app.data.remote

import com.cinephile.app.data.remote.dto.TmdbGenreListDto
import com.cinephile.app.data.remote.dto.TmdbMediaDto
import com.cinephile.app.data.remote.dto.TmdbMovieDetailDto
import com.cinephile.app.data.remote.dto.TmdbPageDto
import com.cinephile.app.data.remote.dto.TmdbPersonPageDto
import com.cinephile.app.data.remote.dto.TmdbTvDetailDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/** TMDB v3 REST API (api_key added centrally by the OkHttp interceptor). */
interface TmdbApi {

    @GET("trending/movie/{window}")
    suspend fun trendingMovies(
        @Path("window") window: String = "week",
        @Query("page") page: Int = 1,
    ): TmdbPageDto

    @GET("trending/tv/{window}")
    suspend fun trendingTv(
        @Path("window") window: String = "week",
        @Query("page") page: Int = 1,
    ): TmdbPageDto

    @GET("movie/popular")
    suspend fun popularMovies(@Query("page") page: Int = 1): TmdbPageDto

    @GET("movie/upcoming")
    suspend fun upcomingMovies(@Query("page") page: Int = 1): TmdbPageDto

    @GET("movie/top_rated")
    suspend fun topRatedMovies(@Query("page") page: Int = 1): TmdbPageDto

    @GET("movie/{id}")
    suspend fun movieDetails(
        @Path("id") id: Int,
        @Query("append_to_response") append: String = "credits,videos,release_dates",
    ): TmdbMovieDetailDto

    @GET("tv/{id}")
    suspend fun tvDetails(
        @Path("id") id: Int,
        @Query("append_to_response") append: String = "credits,videos",
    ): TmdbTvDetailDto

    @GET("movie/{id}/similar")
    suspend fun similarMovies(@Path("id") id: Int, @Query("page") page: Int = 1): TmdbPageDto

    @GET("movie/{id}/recommendations")
    suspend fun recommendedMovies(@Path("id") id: Int, @Query("page") page: Int = 1): TmdbPageDto

    @GET("tv/{id}/similar")
    suspend fun similarTv(@Path("id") id: Int, @Query("page") page: Int = 1): TmdbPageDto

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("include_adult") includeAdult: Boolean = false,
    ): TmdbPageDto

    @GET("search/tv")
    suspend fun searchTv(
        @Query("query") query: String,
        @Query("page") page: Int = 1,
    ): TmdbPageDto

    @GET("search/person")
    suspend fun searchPeople(
        @Query("query") query: String,
        @Query("page") page: Int = 1,
    ): TmdbPersonPageDto

    @GET("discover/movie")
    suspend fun discoverMovies(
        @Query("page") page: Int = 1,
        @Query("sort_by") sortBy: String? = null,
        @Query("with_genres") withGenres: String? = null,
        @Query("without_genres") withoutGenres: String? = null,
        @Query("primary_release_year") primaryReleaseYear: Int? = null,
        @Query("primary_release_date.gte") releaseDateGte: String? = null,
        @Query("primary_release_date.lte") releaseDateLte: String? = null,
        @Query("vote_average.gte") voteAverageGte: Double? = null,
        @Query("vote_average.lte") voteAverageLte: Double? = null,
        @Query("vote_count.gte") voteCountGte: Int? = null,
        @Query("vote_count.lte") voteCountLte: Int? = null,
        @Query("with_people") withPeople: String? = null,
        @Query("with_runtime.gte") runtimeGte: Int? = null,
    ): TmdbPageDto

    @GET("genre/movie/list")
    suspend fun movieGenres(): TmdbGenreListDto
}
