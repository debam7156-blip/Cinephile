package com.cinephile.app

import android.app.Application

/** Application entry point. Owns the manual dependency container. */
class CinephileApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }

    companion object {
        lateinit var container: AppContainer
            private set
    }
}
