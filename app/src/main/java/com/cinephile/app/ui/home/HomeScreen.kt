package com.cinephile.app.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cinephile.app.ui.components.ErrorState
import com.cinephile.app.ui.components.LoadingState
import com.cinephile.app.ui.components.MediaRow
import com.cinephile.app.domain.model.MediaItem
import com.cinephile.app.domain.model.TrackedMedia

@Composable
fun HomeScreen(
    state: HomeState,
    onRetry: () -> Unit,
    onMediaClick: (MediaItem) -> Unit,
) {
    when (state) {
        is HomeState.Loading -> LoadingState(Modifier.fillMaxSize())
        is HomeState.Failed -> ErrorState(message = state.message, onRetry = onRetry)
        is HomeState.Ready -> LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item {
                Text(
                    text = "Cinephile",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                )
            }
            item {
                MediaRow(
                    title = "Trending Now",
                    items = state.trending,
                    error = state.rowErrors["trending"],
                    onClick = onMediaClick,
                )
            }
            item {
                MediaRow(
                   , title = "Popular",
                    items = state.popular,
                    error = hasError("popular", state),
                    onClick = onMediaClick,
                )
            }
            item {
                MediaRow(
                    title = "Upcoming Releases",
                    items = state.upcoming,
                    error = hasError("upcoming", state),
                    onClick = onMediaClick,
                )
            }
  item {
                MediaRow(
                    title = "Top Rated",
                    items = state.topRated,
                    error = hasError("topRated", state),
                    onClick = onMediaClick,
                )
            }
            if (state.recentlyWatched.isNotEmpty()) {
                item {
                    TrackedRow("Recently Watched", state.recentlyWatched) { onMediaClick(it) }
                }
            }
        }
    }
}

@Composable
private fun hasError(key: String, state: HomeState.Ready): String? = state.rowErrors[key]

@Composable
private fun TrackedRow(
    title: String,
    items: List<TrackedMedia>,
    onClick: (MediaItem) -> Unit,
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
    ) {
        items(items, key = { it.type.name + ":" + it.tmdbId }) { tracked ->
            PosterCard(
                item = tracked.toMediaItem(),
                onClick = onClick,
            )
        }
    }
}

private fun TrackedMedia.toMediaItem(): MediaItem = MediaItem(
    id = tmdbId,
    type = type,
    title = title,
    overview = overview,
    posterPath = posterPath,
    backdropPath = backdropPath,
    releaseDate = releaseDate,
    voteAverage = voteAverage,
    voteCount = 0,
    popularity = 0.0,
    genreIds = emptyList(),
)
