package com.movieDB.movies

import android.app.Application
import android.content.Context

class MovieAppClass : Application() {

    companion object {
        lateinit var appContext: Context
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
    }

}