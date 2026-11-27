package com.cinephile.ui

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Ink = Color(0xFF101015)
private val Panel = Color(0xFF1B1B22)
private val Accent = Color(0xFFE6B75A)

@Composable
fun CinephileTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = darkColorScheme(primary = Accent, secondary = Color(0xFFBCA8E8), background = Ink, surface = Panel, onPrimary = Ink, onBackground = Color(0xFFF4F1EA), onSurface = Color(0xFFF4F1EA)), content = content)
}
