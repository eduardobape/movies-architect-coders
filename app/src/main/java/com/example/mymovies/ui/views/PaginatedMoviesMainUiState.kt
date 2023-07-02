package com.example.mymovies.ui.views

import com.example.mymovies.domain.models.MoviesDiscoveryDetails
import com.example.mymovies.ui.models.MoviesDiscoveryFilters

data class PaginatedMoviesMainUiState(
    val isLoading: Boolean = false,
    val moviesDiscoveryDetails: MoviesDiscoveryDetails = MoviesDiscoveryDetails(),
    val moviesDiscoveryFilters: MoviesDiscoveryFilters = MoviesDiscoveryFilters()
)