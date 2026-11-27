package com.cinephile.app.ui.navigation

import androidx.compose.runtime.Composable
import com.cinephile.app.ui.components.EmptyState

@Composable
fun AssistantRoute() {
    EmptyState(
        message = "AI Assistant",
        hint = "Your personal movie assistant — coming in the next milestone.",
    )
}
