# Cinephile

An all-in-one Android movie tracking and discovery app: browse trending films, track what you watch, rate and review, get AI-powered recommendations, and see rich personal statistics.

## Status

In active development on the development branch. Built in stages; every commit is expected to compile.

- Stage 1 done: project structure, Gradle setup, theme, CI-ready build config
- Stage 2 in progress: TMDB discovery, home screen rows, navigation shell with bottom bar
- Stage 3: tracking, watchlist, ratings, reviews (repository and Room layer done, UI next)
- Stage 4: cloud sync via Supabase (schema designed with revision and dirty flags plus clientUuid for conflict resolution)
- Stage 5: AI recommendations and assistant
- Stage 6: statistics and import/export (Letterboxd zip and csv reader already implemented)
- Stage 7: testing, optimization, release build

## Architecture

- UI: Jetpack Compose, Material 3, dark theme by default
- Presentation: MVVM with StateFlow
- Data: repository pattern over Room (local, offline-first) and Retrofit (TMDB)
- DI: manual constructor injection via AppContainer
- Navigation: Navigation Compose

## Setup

1. Clone and open in Android Studio (Koala or newer, AGP 8.5, JDK 17).
2. Copy local.properties.example to local.properties and fill in your TMDB key.
3. Sync Gradle and run on an emulator or device (minSdk 27).

Get a free TMDB API key at https://www.themoviedb.org/settings/api

API keys are never committed. local.properties is gitignored, and secrets are baked into BuildConfig at build time, or provided via environment variables for CI.

## Building

./gradlew assembleDebug
./gradlew testDebugUnitTest

Note: the Gradle wrapper jar is not committed. On a fresh clone run: gradle wrapper --gradle-version 8.9

## Testing

Unit tests cover the tracking repository (duplicate prevention, rating clamping and editing, watchlist semantics, favourite toggling, revision counters) and core utilities (formatting, error humanization).
