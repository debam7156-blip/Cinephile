package com.cinephile.app.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cinephile.app.core.util.DataResult
import com.cinephile.app.data.repository.TmdbRepository
import com.cinephile.app.data.repository.TrackingRepository
import com.cinephile.app.domain.model.MediaDetails
import com.cinephile.app.domain.model.MediaType
import com.cinephile.app.domain.model.TrackedMedia
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed interface DetailsState {
    data object Loading : DetailsState
    data class Ready(val details: MediaDetails, val tracked: TrackedMedia?) : DetailsState
    data class Failed(val message: String) : DetailsState
}

class DetailsViewModel(
    private val tmdb: TmdbRepository,
    private val tracking: TrackingRepository,
    private val id: Int,
    private val type: MediaType,
) : ViewModel() {

    private val _state = MutableStateFlow<DetailsState>(DetailsState.Loading)
    val state: StateFlow<DetailsState> = _state

    init {
        load()
        viewModelScope.launch {
            tracking.observeOne(id, type).collect { t ->
                val s = _state.value
                if (s is DetailsState.Ready) _state.value = s.copy(tracked = t)
            }
        }
    }

    fun load() {
        _state.value = DetailsState.Loading
        viewModelScope.launch {
            val result = if (type == MediaType.TV) tmdb.tvDetails(id) else tmdb.movieDetails(id)
            _state.value = when (result) {
                is DataResult.Success -> DetailsState.Ready(result.data, tracking.getOne(id, type))
                is DataResult.Failure -> DetailsState.Failed(result.message)
            }
        }
    }

    private fun withDetails(block: suspend (MediaDetails) -> Unit) {
        val s = _state.value
        if (s is DetailsState.Ready) viewModelScope.launch { block(s.details) }
    }

    fun markWatched() = withDetails { tracking.markWatched(it) }

    fun toggleWatchlist() = withDetails { d ->
        val t = tracking.getOne(d.id, d.type)
        if (t?.onWatchlist == true) tracking.removeFromWatchlist(d) else tracking.addToWatchlist(d)
    }

    fun toggleFavourite() = withDetails { tracking.toggleFavourite(it) }

    fun setRating(rating: Int) = withDetails { tracking.setRating(it, rating) }

    fun setReview(review: String) = withDetails { tracking.setReview(it, review) }
}
