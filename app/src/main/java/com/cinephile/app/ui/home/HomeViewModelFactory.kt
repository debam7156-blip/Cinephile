package com.cinephile.app.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cinephile.app.AppContainer
import com.cinephile.app.CinephileApp

/** Creates ViewModels with repositories from the application container. */
class HomeViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        HomeViewModel(
            tmdb = CinephileApp.container.tmdbRepository,
            tracking = CinephileApp.container.trackingRepository,
        ) as T
}
