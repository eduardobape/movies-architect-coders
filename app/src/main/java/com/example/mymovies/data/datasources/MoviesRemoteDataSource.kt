package com.example.mymovies.data.datasources

import arrow.core.Either
import com.example.mymovies.domain.Error
import com.example.mymovies.domain.Movie
import com.example.mymovies.domain.MovieImageSize
import com.example.mymovies.domain.MoviesSearchFilters
import com.example.mymovies.domain.PaginatedMovies

interface MoviesRemoteDataSource {

    suspend fun requestPaginatedMovies(
        moviesSearchFilters: MoviesSearchFilters,
        pageToFetch: Int
    ): Either<Error, PaginatedMovies>

    suspend fun requestMovieDetails(movieId: Long): Either<Error, Movie>
    fun buildUrlMovieImage(movieImageRelativePathUrl: String, imageSize: MovieImageSize): String
}
