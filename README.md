# Cinephile

Cinephile is an Android personal movie journal. The initial foundation uses Kotlin, Jetpack Compose, MVVM, and Room to store watched films and watchlists locally.

## Run locally

Open in Android Studio with JDK 17 and Android SDK 35, sync Gradle, then run the `app` configuration on Android 8.0+ (API 26+). No service keys or login are needed for local tracking.

## Available in this foundation

- Offline Room-backed personal log.
- Add, edit, rate, review, remove entries; set watched or watchlist status.
- Dark Compose interface with Discover, Library filters, and local statistics.
- Empty states and basic form validation.

## Not yet connected

Live search/posters/trending require a movie data provider such as TMDB. Cloud sync/auth and hosted AI are not enabled in this milestone; adding them requires backend/provider setup and secrets configured outside the Android app. No fake movie catalog is bundled.

## Build

Run `gradle :app:assembleDebug` after Android SDK setup. GitHub Actions automation is planned; this repository integration could not write the workflow file in this pass. Add the Gradle wrapper from Android Studio for reproducible local builds.

## Secret handling

Do not commit API keys, signing keys, local properties, `.env`, or service account files. `.gitignore` excludes common credentials and build outputs.
