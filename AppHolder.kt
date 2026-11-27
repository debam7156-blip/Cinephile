package com.cinephile.app.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cinephile.app.AppContainer

/** Simple factory that pulls repositories from the app container. */
class HomeViewModelFactory(private val container: AppContainer? = null) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return HomeViewModel(
            tmdb = AppHolder.container.tmdbRepository,
            tracking = AppHolder.container.trackingRepository,
        ) as T
    }
}

/** Global holder because the container lives in the Application, not Compose. */
object AppHolder {
    lateinit var container: AppContainer
        private set

    fun init(container: AppContainer) {
        this.container = container
    }
}
