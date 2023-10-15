package com.example.mymovies.data.datasources

import com.example.mymovies.domain.Movie
import com.example.mymovies.domain.MovieImageSize
import com.example.mymovies.domain.MoviesSearchFilters
import com.example.mymovies.domain.PaginatedMovies

interface MoviesRemoteDataSource {

    suspend fun requestPaginatedMovies(moviesSearchFilters: MoviesSearchFilters, pageToFetch: Int): PaginatedMovies
    suspend fun requestMovieDetails(movieId: Long): Movie
    fun buildUrlMovieImage(movieImageRelativePathUrl: String, imageSize: MovieImageSize): String
}
