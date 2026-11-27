package com.cinephile.app.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cinephile.app.core.util.DataResult
import com.cinephile.app.data.repository.TmdbRepository
import com.cinephile.app.data.repository.TrackingRepository
import com.cinephile.app.domain.model.MediaItem
import com.cinephile.app.domain.model.TrackedMedia
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed interface HomeState {
    data object Loading : HomeState
    data class Ready(
        val trending: List<MediaItem>,
        val popular: List<MediaItem>,
        val upcoming: List<MediaItem>,
        val topRated: List<MediaItem>,
        val recentlyWatched: List<TrackedMedia>,
        val rowErrors: Map<String, String>,
    ) : HomeState
    data class Failed(val message: String) : HomeState
}

class HomeViewModel(
    private val tmdb: TmdbRepository,
    private val tracking: TrackingRepository,
) : ViewModel() {

    private val _state = MutableStateFlow<HomeState>(HomeState.Loading)
    val state: StateFlow<HomeState> = _state

    init {
        refresh()
        viewModelScope.launch {
            tracking.observeWatched().collect { watched ->
                val s = _state.value
                if (s is HomeState.Ready) {
                    _state.value = s.copy(recentlyWatched = watched.take(15))
                }
            }
            tracking.observeWatchlist().collect { wl ->
                val s = _state.value
                if (s is HomeState.Ready) {
                    _state.value = s.copy(popular = s.popular, recentlyWatched = s.recentlyWatched)
                }
            }
        }
    }

    fun refresh() {
        _state.value = HomeState.Loading
        viewModelScope.launch {
            val errors = mutableMapOf<String, String>()
            val t = async { safe { tmdb.trendingMovies() } }
            val p = async { safe { tmdb.popularMovies() } }
            val u = async { safe { tmdb.upcomingMovies() } }
            val r = async { safe { tmdb.topRatedMovies() } }
            val trend = t.await()
            val pop = p.await()
            val upc = u.await()
            val top = r.await()
            if (trend is DataResult.Failure) errors["trending"] = trend.message
            if (pop is DataResult.Failure) errors["popular"] = pop.message
            if (upc is DataResult.Failure) errors["upcoming"] = upc.message
            if (top is DataResult.Failure) errors["topRated"] = top.message
            _state.value = HomeState.Ready(
                trending = trend.dataOrEmpty(),
                popular = pop.dataOrEmpty(),
                upcoming = upc.dataOrEmpty(),
                topRated = top.dataOrEmpty(),
                recentlyWatched = emptyList(),
                recentlyWatched = emptyList(),
                rowErrors = errors,
            )
        }
    }

    private suspend fun <T> safe(block: suspend () -> DataResult<T>): DataResult<T> =
        try { block() } catch (t: Throwable) {
            if (t is kotlinx.coroutines.CancellationException) throw t
            DataResult.Failure(com.cin ephile.app.core.util.humanize(t, "Request failed."))
        }

    private fun <T> DataResult<T>.dataOrEmpty(): List<T> =
        (this as? DataResult.Success)?.data as? List<T> ?: emptyList()

    fun onMediaClick(item: MediaItem) { /* navigation via UI */ }
}
