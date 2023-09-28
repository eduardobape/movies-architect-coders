package com.example.mymovies.ui.views

import com.example.mymovies.data.errors.Error
import com.example.mymovies.domain.models.PaginatedMovieDetails
import com.example.mymovies.ui.models.MoviesSearchFilters

data class PaginatedMoviesMainUiState(
    val isLoading: Boolean = false,
    val movies: List<PaginatedMovieDetails> = emptyList(),
    val currentPage: Int = 0,
    val totalPages: Int = 0,
    val moviesSearchFilters: MoviesSearchFilters = MoviesSearchFilters(),
    val error: Error? = null,
    val isNeededRetryCollectMovies: Boolean = false,
    val isNeededRetryFetchMovies: Boolean = false
)
