package com.example.mymovies.data.datasources

import com.example.mymovies.data.errors.FavouriteMovieSQLException
import com.example.mymovies.data.local.database.daos.MovieDao
import com.example.mymovies.data.local.database.entities.Movie
import com.example.mymovies.data.local.database.entities.MovieWithGenres
import com.example.mymovies.data.local.database.entities.MoviesPaginationInfo
import kotlinx.coroutines.flow.Flow

class MoviesLocalDataSource(private val movieDao: MovieDao) {

    val popularMovies: Flow<List<Movie>>
        get() = movieDao.findPopularMovies()

    suspend fun savePaginatedMovies(movies: List<Movie>, moviesPaginationInfo: MoviesPaginationInfo) {
        movieDao.savePaginatedMovies(movies, moviesPaginationInfo)
    }

    suspend fun getMoviesPaginationInfo(): MoviesPaginationInfo? {
        return movieDao.getMoviesPaginationInfo()
    }

    suspend fun saveMovieDetailsWithGenres(movieDetails: MovieWithGenres) {
        movieDao.saveMovie(movieDetails.movie)
        movieDao.saveMovieGenresAndRelation(movieDetails)
    }

    fun getMovieWithGenres(movieId: Long): Flow<MovieWithGenres> = movieDao.findMovieWithGenres(movieId)

    suspend fun switchMovieFavourite(movieId: Long, toFavourite: Boolean) {
        runCatching {
            movieDao.switchMovieFavourite(movieId, toFavourite)
        }.onFailure {
            throw FavouriteMovieSQLException()
        }
    }
}
