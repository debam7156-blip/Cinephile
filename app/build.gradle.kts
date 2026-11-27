import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
}

// Secrets are read from local.properties (gitignored) or environment variables.
// They are baked into BuildConfig at build time and are never committed to the repo.
val localSecrets = Properties()
val localSecretsFile = rootProject.file("local.properties")
if (localSecretsFile.exists()) {
    localSecretsFile.inputStream().use { localSecrets.load(it) }
}

fun buildSecret(name: String): String {
    val fromFile = localSecrets.getProperty(name)
    val fromEnv = System.getenv(name)
    val fromProperty = project.findProperty(name)?.toString()
    return (fromFile ?: fromEnv ?: fromProperty ?: "").trim()
}

android {
    namespace = "com.cinephile.app"
    app_dir = rootProject.layout.projectDirectory.dir("app")
    compileSdk = 34

    defaultConfig {
        applicationId = "com.cinephile.app"
        minSdk = 27
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"

        buildConfigField("String", "TMDB_API_KEY