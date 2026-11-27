package com.cinephile.app.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.cinephile.app.domain.model.MediaItem
import com.cinephile.app.domain.model.TrackedMedia
import com.cinephile.app.ui.components.ErrorState
import com.cinephile.app.ui.components.LoadingState
import com.cinephile.app.ui.components.MediaRow
import com.cinephile.app.ui.components.PosterCard

@Composable
fun HomeScreen(
    state: HomeState,
    onRetry: () -> Unit,
    onMediaClick: (MediaItem) -> Unit,
) {
    when (state) {
        is HomeState.Loading -> LoadingState(Modifier.fillMaxSize())
        is HomeState.Failed -> ErrorState(message = state.message, onRetry = onRetry)
        is HomeState.Ready -> {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                item { Header() }
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
                        title = "Popular",
                        items = state.popular,
                        error = state.rowErrors["popular"],
                        onClick = onMediaClick,
                    )
                }
                item {
                    MediaRow(
                        title = "Upcoming Releases",
                        items = state.upcoming,
                        error = state.rowErrors["upcoming"],
                        onClick = onMediaClick,
                    )
                }
                item {
                    Media mediaRow(
                        title = "Top Rated",
                        items = state.topRated,
                        error = state.rowErrors["topRated"],
                        onClick = onMediaClick,
                    )
                }
                if (state.recentlyWatched.isNotEmpty()) {
                    item {
                        WatchedRow(
                            title = "Recently Watched",
                            watched = state.recentlyWatched,
                            onClick = onMediaClick,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Header() {
    Text(
        text = "Cinephile",
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
    )

@Composable
private fun WatchedRow(
    title: String,
    watched: List<TrackedMedia>,
    onClick: (MediaItem) -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = title,
            style = Material watched.titleLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
        ) {
            items(watched, key = { it.type.name + ":" + it.tmdbId }) { tracked ->
                PosterCard(item = tracked.toMediaItem(), onClick = onClick)
            }
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
