package com.example.mymovies

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.mymovies.data.local.database.AppDataBase

class App : Application() {

    lateinit var database: AppDataBase
        private set

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            this,
            AppDataBase::class.java,
            "app-database"
        ).build()
    }
}

val Context.appContext: App
    get() = applicationContext as App