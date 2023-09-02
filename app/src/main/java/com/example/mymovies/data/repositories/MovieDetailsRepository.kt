package com.example.mymovies.data.repositories

import com.example.mymovies.App
import com.example.mymovies.data.datasources.MoviesLocalDataSource
import com.example.mymovies.data.datasources.MoviesRemoteDataSource
import com.example.mymovies.data.local.database.entities.MovieWithGenres
import com.example.mymovies.data.remote.models.MovieDetailsSearchRemoteResult
import com.example.mymovies.data.remote.models.toDatabaseMovieModel
import com.example.mymovies.data.remote.services.MoviesApiServices
import com.example.mymovies.data.remote.services.RetrofitApiServices
import kotlinx.coroutines.flow.Flow

class MovieDetailsRepository(app: App) {

    private val moviesRemoteDataSource =
        MoviesRemoteDataSource(MoviesApiServices(RetrofitApiServices).moviesSearchApiService)
    private val moviesLocalDataSource = MoviesLocalDataSource(app.database.movieDao())


    fun getMovieDetailsWithGenres(movieId: Long): Flow<MovieWithGenres> =
        moviesLocalDataSource.getMovieWithGenres(movieId)

    suspend fun findMovieDetailsById(movieId: Long) {
        val movieDetailsRemoteResult: MovieDetailsSearchRemoteResult =
            moviesRemoteDataSource.fetchMovieDetailsById(movieId)
        moviesLocalDataSource.saveMovieDetailsWithGenres(movieDetailsRemoteResult.toDatabaseMovieModel())
    }

    suspend fun switchMovieFavourite(movieId: Long, toFavourite: Boolean) {
        moviesLocalDataSource.switchMovieFavourite(movieId, toFavourite)
    }
}
