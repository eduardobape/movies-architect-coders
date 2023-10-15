package com.example.mymovies

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.mymovies.framework.database.AppDataBase
import com.example.mymovies.framework.webservice.RemoteConnection

class App : Application() {

    lateinit var database: AppDataBase
        private set

    override fun onCreate() {
        super.onCreate()
        RemoteConnection.init(this)
        database = Room.databaseBuilder(
            this,
            AppDataBase::class.java,
            "app-database"
        ).build()
    }
}

val Context.appContext: App
    get() = applicationContext as App
