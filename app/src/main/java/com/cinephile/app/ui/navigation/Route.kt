package com.cinephile.app.ui.navigation

import com.cinephile.app.domain.model.MediaType

/** Type-safe navigation routes. */
sealed interface Route {
    data object Home : Route
    data object Search : Route
    data object Library : Route
    data object Stats : Route
    data object Assistant : Route
    data object Settings : Route
    data class Details(val id: Int, val type: MediaType) : Route

    fun path(): String = when (this) {
        is Home -> "home"
        is Search -> "search"
        is Library -> "library"
        is Stats -> "stats"
        is Assistant -> "assistant"
        is Details -> "details/movie/" + id
        is Settings -> "settings"
    }
}
