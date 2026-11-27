package com.cinephile.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val CinephileDarkColors = darkColorScheme(
    primary = Amber,
    onPrimary = BackgroundDark,
    primaryContainer = SurfaceElevatedDark,
    onPrimaryContainer = Amber,
    secondary = Teal,
    onSecondary = BackgroundDark,
    tertiary = Coral,
    onTertiary = BackgroundDark,
    background = BackgroundDark,
    onBackground = OnBackgroundDark,
    surface = SurfaceDark,
    onSurface = OnSurfaceDark,
    surfaceVariant = SurfaceElevatedDark,
    onSurfaceVariant = OnSurfaceVariantDark,
    outline = OutlineDark,
    error = ErrorDark,
    onError = BackgroundDark,
)

/**
 * Cinephile is dark-theme-first by design. A dark palette is always applied so
 * posters and backdrops render against a cinematic background, matching the
 * product's streaming-platform aesthetic.
 */
@Composable
fun CinephileTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = CinephileDarkColors,
        typography = CinephileTypography,
        content = content,
    )
}
