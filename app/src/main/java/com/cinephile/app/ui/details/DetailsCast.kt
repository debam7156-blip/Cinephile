package com.cinephile.app.ui.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.cinephile.app.core.util.Formatters
import com.cinephile.app.domain.model.MediaDetails

@Composable
fun DetailsCast(d: MediaDetails) {
    if (d.cast.isEmpty()) return
    Column(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
        Text("Cast", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onBackground, modifier = Modifier.padding(horizontal = 16.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp), contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp, vertical = 8.dp)) {
            items(d.cast, key = { it.id }) { member ->
                Column(horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
                    val url = Formatters.profileUrl(member.profilePath)
                    if (url != null) {
                        AsyncImage(model = url, contentDescription = member.name, contentScale = ContentScale.Crop, modifier = Modifier.size(64.dp).clip(CircleShape))
                    } else {
                        androidx.compose.foundation.layout.Box(modifier = Modifier.size(64.dp).clip(CircleShape), contentAlignment = androidx.compose.ui.Alignment.Center) {
                            Text(member.name.take(1), style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                    Text(member.name, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.padding(top = 4.dp))
                }
            }
        }
    }
}
