package com.example.mymovies.ui.views

import com.example.mymovies.domain.models.MovieMainDetails
import com.example.mymovies.ui.models.MoviesDiscoveryFilters

data class PaginatedMoviesMainUiState(
    val isLoading: Boolean = false,
    val movies: List<MovieMainDetails> = emptyList(),
    val currentPage: Int = 0,
    val totalPages: Int = 0,
    val moviesDiscoveryFilters: MoviesDiscoveryFilters = MoviesDiscoveryFilters()
)