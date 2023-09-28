package com.example.mymovies.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mymovies.data.local.database.daos.MovieDao
import com.example.mymovies.data.local.database.entities.Movie
import com.example.mymovies.data.local.database.entities.MovieGenre
import com.example.mymovies.data.local.database.entities.MoviesGenresCrossRef
import com.example.mymovies.data.local.database.entities.MoviesPaginationInfo

@Database(
    entities = [Movie::class, MovieGenre::class, MoviesGenresCrossRef::class, MoviesPaginationInfo::class],
    version = 1,
    exportSchema = false
)
abstract class AppDataBase : RoomDatabase() {

    abstract fun movieDao(): MovieDao
}
