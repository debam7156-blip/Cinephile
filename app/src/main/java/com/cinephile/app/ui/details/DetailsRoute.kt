package com.cinephile.app.ui.details

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cinephile.app.CinephileApplication
import com.cinephile.app.domain.model.MediaType

@Composable
fun DetailsRoute(id: Int, type: MediaType) {
    val factory = remember {
        object : androidx.lifecycle.ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T =
                DetailsViewModel(
                    tmdb = CinephileApplication.container.tmdbRepository,
                    tracking = CinephileApplication.container.trackingRepository,
                    id = id,
                    type = type,
                ) as T
        }
    }
    val vm: DetailsViewModel = viewModel(key = "details:$id:${type.name}", factory = factory)
    val state = vm.state.collectAsState().value
    DetailsScreen(
        state = state,
        onRetry = { vm.load() },
        onMarkWatched = { vm.markWatched() },
        onToggleWatchlist = { vm.toggleWatchlist() },
        onToggleFavourite = { vm.toggleFavourite() },
        onRate = { vm.setRating(it) },
        onReview = { vm.setReview(it) },
    )
}
