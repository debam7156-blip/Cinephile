package com.cinephile

import android.app.Application
import androidx.room.Room
import com.cinephile.data.CinephileDatabase
import com.cinephile.data.MovieRepository

class CinephileApplication : Application() {
    val database: CinephileDatabase by lazy {
        Room.databaseBuilder(applicationContext, CinephileDatabase::class.java, "cinephile.db").build()
    }
    val movieRepository: MovieRepository by lazy { MovieRepository(database.movieDao()) }
}
