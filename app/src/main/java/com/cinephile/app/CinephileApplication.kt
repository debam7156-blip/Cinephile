package com.cinephile.app

import android.app.Application

/** Application entry point. Owns the manual dependency container. */
class CinephileApplication : Application() {

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
