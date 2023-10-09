package com.example.mymovies.data.datasources

import com.example.mymovies.data.errors.FavouriteMovieSQLException
import com.example.mymovies.data.local.database.daos.MovieDao
import com.example.mymovies.data.local.database.entities.Movie
import com.example.mymovies.data.local.models.Movie as MovieLocalModel
import com.example.mymovies.data.local.database.entities.MovieWithGenres
import com.example.mymovies.data.local.database.entities.MoviesPaginationInfo
import com.example.mymovies.data.local.models.toMovieGenreModelDatabase
import com.example.mymovies.data.local.models.toMovieModelDatabase
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

    suspend fun saveMovieDetailsWithGenres(movie: MovieLocalModel) {
        movieDao.saveMovie(movie.toMovieModelDatabase())
        movieDao.saveMovieGenresAndRelation(movie.id, movie.genres.map { it.toMovieGenreModelDatabase() })
    }

    fun getMovieWithGenres(movieId: Long): Flow<MovieWithGenres> = movieDao.findMovie(movieId)

    suspend fun switchMovieFavourite(movieId: Long, toFavourite: Boolean) {
        runCatching {
            movieDao.switchMovieFavourite(movieId, toFavourite)
        }.onFailure {
            throw FavouriteMovieSQLException()
        }
    }

    suspend fun isMovieFlaggedAsFavourite(movieId: Long): Boolean = movieDao.isMovieFlaggedAsFavourite(movieId)
}
