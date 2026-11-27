package com.cinephile.app.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cinephile.app.core.util.DataResult
import com.cinephile.app.core.util.humanize
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
        val trending: List<MediaItem> = emptyList(),
        val popular: List<MediaItem> = emptyList(),
        val upcoming: List<MediaItem> = emptyList(),
        val topRated: List<MediaItem> = emptyList(),
        val recentlyWatched: List<TrackedMedia> = emptyList(),
        val rowErrors: Map<String, String> = emptyMap(),
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
        }
    }

    fun refresh() {
        _state.value = HomeState.Loading
        viewModelScope.launch {
            val t = async { safe { tmdb.trendingMovies() } }
            val p = async { safe { tmdb.popularMovies() } }
            val u = async { safe { tmdb.upcomingMovies() } }
            val r = async { safe { tmdb.topRatedMovies() } }
            val trend = t.await()
            val pop = p.await()
            val upc = u.await()
            val top = r.await()
            val errors = mutableMapOf<String, String>()
            (trend as? DataResult.Failure)?.let { errors["trending"] = it.message }
            (pop as? DataResult.Failure)?.let { errors["popular"] = it.message }
            (upc as? DataResult.Failure)?.let { errors["upcoming"] = it.message }
            (top as? DataResult.Failure)?.let { errors["topRated"] = it.message }
            _state.value = HomeState.Ready(
                trending = list(trend),
                popular = list(pop),
                upcoming = list(upc),
                topRated = list(top),
                rowErrors = errors,
            )
        }
    }

    private fun list(r: DataResult<List<MediaItem>>): List<MediaItem> =
        (r as? DataResult.Success)?.data ?: emptyList()

    private suspend fun <T> safe(block: suspend () -> DataResult<T>): DataResult<T> =
        try {
            block()
        } catch (t: Throwable) {
            if (t is kotlinx.coroutines.CancellationException) throw t
            DataResult.Failure(humanize(t, "Request failed."))
        }
}
