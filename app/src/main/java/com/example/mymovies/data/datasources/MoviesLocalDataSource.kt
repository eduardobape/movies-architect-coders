package com.example.mymovies.data.datasources

import com.example.mymovies.data.local.database.daos.MovieDao
import com.example.mymovies.data.local.database.entities.Movie
import kotlinx.coroutines.flow.Flow

class MoviesLocalDataSource(private val movieDao: MovieDao) {

    val popularMovies: Flow<List<Movie>>
        get() = movieDao.findPopularMovies()

    suspend fun saveMovies(movies: List<Movie>) {
        movieDao.saveMovies(movies)
    }
}
