package com.example.mymovies.data.datasources

import com.example.mymovies.data.local.database.daos.MovieDao
import com.example.mymovies.data.local.database.entities.Movie
import com.example.mymovies.data.local.database.entities.MovieWithGenres
import kotlinx.coroutines.flow.Flow

class MoviesLocalDataSource(private val movieDao: MovieDao) {

    val popularMovies: Flow<List<Movie>>
        get() = movieDao.findPopularMovies()

    suspend fun saveMovies(movies: List<Movie>) {
        movieDao.saveMovies(movies)
    }

    suspend fun saveMovieDetailsWithGenres(movieDetails: MovieWithGenres) {
        movieDao.saveMovieDetailsWithGenres(movieDetails)
    }

    fun getMovieWithGenres(movieId: Long): Flow<MovieWithGenres> = movieDao.findMovieWithGenres(movieId)

    suspend fun switchMovieFavourite(movieId: Long, toFavourite: Boolean) {
        movieDao.switchMovieFavourite(movieId, toFavourite)
    }
}
