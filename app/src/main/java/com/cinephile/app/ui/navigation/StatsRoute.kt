package com.cinephile.app.ui.navigation

import androidx.compose.runtime.Composable
import com.cinephile.app.ui.components.EmptyState

@Composable
fun StatsRoute() {
    EmptyState(
        message = "Statistics",
        hint = "Watch statistics and visualisations — coming in the next milestone.",
    )
}
