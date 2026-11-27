package com.cinephile.app.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector

/** Bottom navigation destinations. */
enum class BottomDest(
    val route: String,
    val label: String,
    val icon: ImageVector,
) {
    HOME("home", "Home", Icons.Filled.Home),
    SEARCH("search", "Search", Icons.Filled.Search),
    LIBRARY("library", "Library", Icons.Filled.Movie),
    STATS("stats", "Stats", Icons.Filled.BarChart),
    ASSISTANT("assistant", "Assistant", Icons.Filled.Star),
}
