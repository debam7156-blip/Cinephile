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
    compileSdk = 34

    defaultConfig {
        applicationId = "com.cinephile.app"
        minSdk = 27
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"

        buildConfigField("String", "TMDB_API_KEY", "\"${buildSecret("TMDB_API_KEY")}\"")
        buildConfigField("String", "SUPABASE_URL", "\"${buildSecret("SUPABASE_URL")}\"")
        buildConfigField("String", "SUPABASE_ANON_KEY", "\"${buildSecret("SUPABASE_ANON_KEY")}\"")
        buildConfigField("String", "LLM_BASE_URL", "\"${buildSecret("LLM_BASE_URL")}\"")
        buildConfigField("String", "LLM_API_KEY", "\"${buildSecret("LLM_API_KEY")}\"")
        buildConfigField("String", "LLM_MODEL", "\"${buildSecret("LLM_MODEL")}\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        debug {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.navigation.compose)

    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material3)
    implementation(libs.compose.material.icons)
    debugImplementation(libs.compose.ui.tooling)

    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    implementation(libs.retrofit)
    implementation(libs.retrofit.moshi)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)
    implementation(libs.moshi.kotlin)

    implementation(libs.datastore.preferences)
    implementation(libs.coil.compose)
    implementation(libs.coroutines.android)

    testImplementation(libs.junit)
    testImplementation(libs.truth)
    testImplementation(libs.turbine)
    testImplementation(libs.coroutines.test)
}
