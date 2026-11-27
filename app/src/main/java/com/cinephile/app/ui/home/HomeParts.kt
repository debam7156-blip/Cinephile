package com.cinephile.app.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import com.cinephile.app.ui.components.PosterCard

@Composable
fun Header() {
    Text("Cinephile", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
}

@Composable
fun WatchedRow(watched: List<TrackedMedia>, onClick: (MediaItem) -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text("Recently Watched", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onBackground, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp), contentPadding = PaddingValues(horizontal = 16.dp)) {
            items(watched, key = { it.type.name + ":" + it.tmdbId }) { tracked ->
                PosterCard(item = tracked.toMediaItem(), onClick = onClick)
            }
        }
    }
}

fun TrackedMedia.toMediaItem(): MediaItem = MediaItem(id = tmdbId, type = type, title = title, overview = overview, posterPath = posterPath, backdropPath = backdropPath, releaseDate = releaseDate, voteAverage = voteAverage, voteCount = 0, popularity = 0.0, genreIds = emptyList())
