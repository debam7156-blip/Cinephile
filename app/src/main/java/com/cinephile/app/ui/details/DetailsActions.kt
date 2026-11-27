package com.cinephile.app.ui.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cinephile.app.domain.model.TrackedMedia

@Composable
fun DetailsActions(
    tracked: TrackedMedia?,
    onMarkWatched: () -> Unit,
    onToggleWatchlist: () -> Unit,
    onToggleFavourite: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text("Your tracking", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onBackground)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(top = 8.dp)) {
            FilterChip(selected = tracked?.watched == true, onClick = onMarkWatched, label = { Text(if (tracked?.watched == true) "Watched" else "Mark watched") })
            FilterChip(selected = tracked?.onWatchlist == true, onClick = onToggleWatchlist, label = { Text(if (tracked?.onWatchlist == true) "On watchlist" else "Add to watchlist") })
            FilterChip(selected = tracked?.favourite == true, onClick = onToggleFavourite, label = { Text(if (tracked?.favourite == true) "Favourite" else "Mark favourite") })
        }
    }
}
