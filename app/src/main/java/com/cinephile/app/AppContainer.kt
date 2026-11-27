package com.cinephile.app

import android.content.Context
import com.cinephile.app.BuildConfig
import com.cinephile.app.data.local.CinephileDatabase
import com.cinephile.app.data.remote.NetworkModule
import com.cinephile.app.data.repository.DiscoverRepository
import com.cinephile.app.data.repository.TmdbRepository
import com.cinephile.app.data.repository.TrackingRepository

/**
 * Manual dependency container: explicit wiring, no codegen, trivially testable.
 */
class AppContainer(context: Context) {

    val database: CinephileDatabase = CinephileDatabase.get(context)

    val tmdbApi = NetworkModule.tmdbApi()

    private val apiKeyPresent: () -> Boolean = { BuildConfig.TMDB_API_KEY.isNotBlank() }

    val tmdbRepository = TmdbRepository(tmdbApi, apiKeyPresent)

    val discoverRepository = DiscoverRepository(tmdbApi, apiKeyPresent)

    val trackingRepository = TrackingRepository(database.trackedMediaDao())
}
