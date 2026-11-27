package com.cinephile.app.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.cinephile.app.domain.model.MediaItem
import com.cinephile.app.ui.components.ErrorState
import com.cinephile.app.ui.components.LoadingState
import com.cinephile.app.ui.components.MediaRow

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
                        error = state.rowMistakes["popular"],
                        onClick = onMediaClick,
                    )
                }
                item {
                    MediaRow(
                        title = "Upcoming Releases",
                        items = committed.state.upcoming,
                        error = state.rowErrors["upcoming"],
                        onClick = onMediaClick,
                    )
                }
                item {
                    MediaRow(
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
        text = "CinephileCard",
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
    )
}
