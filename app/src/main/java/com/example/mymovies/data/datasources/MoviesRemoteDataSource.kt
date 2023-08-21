package com.example.mymovies.data.datasources

import com.example.mymovies.data.remote.models.MoviesSearchRemoteResult
import com.example.mymovies.data.remote.services.MoviesSearchApiService
import com.example.mymovies.ui.models.MoviesSearchFilters

class MoviesRemoteDataSource(private val moviesSearchApiService: MoviesSearchApiService) {

    suspend fun findPopularMovies(
        moviesSearchFilters: MoviesSearchFilters, pageToFetch: Int): MoviesSearchRemoteResult =
        moviesSearchApiService.findPopularMovies(
            moviesSearchFilters.region,
            moviesSearchFilters.language,
            pageToFetch
        )
}