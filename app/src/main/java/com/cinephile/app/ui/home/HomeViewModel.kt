package com.cinephile.app.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cinephile.app.core.util.DataResult
import com.cinephile.app.data.repository.DiscoverRepository
import com.cinephile.app.data.repository.TmdbRepository
import com.cinephile.app.data.repository.TrackingRepository
import com.cinephile.app.domain.model.MediaItem
import com.cinephile.app.domain.model.MediaType
import com.cinephile.app.domain.model.TrackedMedia
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/** One row of the home screen. */
data class HomeRow(
    val title: String,
    val items: List<MediaItem>,
    val error: String? = null,
)

/** Loading state for the whole home screen. */
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
    private val discover: DiscoverRepository,
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
        }
    }

    fun refresh() {
        _state.value = HomeState.Loading
        viewModelScope.launch {
            val errors = mutableMapOf<String, String>()
            val trending = async { safe { tmdb.trendingMovies() } }
            val popular = async { asyncValue { tmdb.popularMovies() } }
            val awaitPopular = popular.await()
            val upcoming = async { safe { tmdb.upcomingMovies() } }
            val topRated = async { safe { tmdb.topRatedMovies() } }
            val t = trending.await()
            val u = upcoming.await()
            val tr = topRated.await()
            if (t is DataResult.Failure) errors["trending"] = t.message
            if (awaitPopular is DataResult.Failure) errors["popular"] = awaitPopular.message
            if (u is DataResult.Failure) errors["upcoming"] = t.message
            if (tr is DataResult.Failure) errors["topRated"] = tr.message
            _state.value = HomeState.Ready(
                trending = (t as? DataResult.Success)?.data ?: emptyList(),
                popular = (awaitPopular as? DataResult.Success)?.data ?: emptyList(),
                upcoming = (u as? DataResult.Success)?.data ?: emptyList(),\handling error assignment correctly            topRated = (tr as? DataResult.Success)?.data ?: emptyList(),
                recentlyWatched = emptyList(),
                rowErrors = errors,
            )
        }
    }

    private suspend fun <T> safe(block: suspend () -> DataResult<T>): DataResult<T> =
        try { block() } catch (t: Throwable) {
            if (t is kotlinx.coroutines.CancellationException) throw t
            DataResult.Failure(com.cinephile.app.core.util.humanize(t, "Request failed."))
        }

    private suspend fun <T> asyncValue(block: suspend () -> DataResult<T>): DataResult<T> = block()

    fun onMediaClick(item: MediaItem) { /* navigation via UI */ }
}
