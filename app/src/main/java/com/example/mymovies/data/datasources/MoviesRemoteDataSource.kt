package com.example.mymovies.data.datasources

import com.example.mymovies.data.remote.models.MovieDetailsSearchRemoteResult
import com.example.mymovies.data.remote.models.PaginatedMoviesSearchRemoteResult
import com.example.mymovies.data.remote.services.MoviesSearchApiService
import com.example.mymovies.ui.models.MoviesSearchFilters
import java.util.Locale

class MoviesRemoteDataSource(private val moviesSearchApiService: MoviesSearchApiService) {

    suspend fun findPopularMovies(
        moviesSearchFilters: MoviesSearchFilters, pageToFetch: Int
    ): PaginatedMoviesSearchRemoteResult =
        moviesSearchApiService.findPopularMovies(
            moviesSearchFilters.region,
            moviesSearchFilters.language,
            pageToFetch
        )

    suspend fun fetchMovieDetailsById(movieId: Long): MovieDetailsSearchRemoteResult {
        return moviesSearchApiService.fetchMovieDetails(movieId, Locale.getDefault().language)
    }
}
