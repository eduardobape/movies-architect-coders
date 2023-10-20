package com.example.mymovies.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Movie::class, MovieGenre::class, MoviesGenresCrossRef::class, MoviesPaginationInfo::class],
    version = 1,
    exportSchema = false
)
abstract class AppDataBase : RoomDatabase() {

    abstract fun movieDao(): MovieDao
}
