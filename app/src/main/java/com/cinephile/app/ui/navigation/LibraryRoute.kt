package com.cinephile.app.ui.navigation

import androidx.compose.runtime.Composable
import com.cinephile.app.ui.components.EmptyState

@Composable
fun LibraryRoute() {
    EmptyState(
        message = "Library",
        hint = "Your watched films, watchlist and custom lists — coming in the next milestone.",
    )
}
