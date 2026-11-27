package com.cinephile.app

import android.app.Application

/** Application entry point. Owns the manual dependency container. */
class CinephileApp : Application() {

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }

    companion object {
        lateinit var container: AppContainer
            private set

        fun init(app: Application) {
            container = AppContainer(app)
        }
    }
}
