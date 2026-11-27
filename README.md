# Cinephile

Cinephile is an Android personal movie journal. This foundation milestone uses Kotlin, Jetpack Compose, MVVM, and Room for local-first tracking.

## Run locally

Open in Android Studio with JDK 17 and Android SDK 35, sync Gradle, then run the `app` configuration on Android 8.0+ (API 26+). Local tracking requires no key or account.

## Included

- Room-backed offline movie log.
- Add, edit, rate, review, remove entries and set watched/watchlist status.
- Dark Compose UI: Discover, Library with status filters, and personal stats.
- Empty states and basic validation.

## Not connected in this milestone

Live movie search, posters and trending content require a movie-data provider configuration. Authentication, cloud sync and hosted AI are not connected. No bundled sample catalog or pretend service responses are used.

## Build and tests

Run `gradle :app:testDebugUnitTest` and `gradle :app:assembleDebug` after installing the Android SDK. The repository connector could not complete writing the GitHub Actions workflow in this pass, so automated build status is not yet available. Add the Gradle wrapper from Android Studio for reproducible local builds.

## Secret handling

Do not commit API keys, signing credentials, local properties, `.env`, or service account files. `.gitignore` excludes common secrets and build outputs.
