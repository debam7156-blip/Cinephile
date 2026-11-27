package com.cinephile.app.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cinephile.app.CinephileApplication

/** Creates ViewModels with repositories from the application container. */
class HomeViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        HomeViewModel(
            tmdb = CinephileApplication.container.tmdbRepository,
            tracking = CinephileApplication.container.trackingRepository,
        ) as T
}
