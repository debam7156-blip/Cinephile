package com.cinephile.app.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cinephile.app.ui.components.EmptyState

@Composable
fun SearchRoute() {
    EmptyState(
        message = "Search",
        hint = "Search movies, TV shows, actors and directors — coming in the next milestone.",
    )
}
