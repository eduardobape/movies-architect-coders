package com.example.mymovies.ui.views

import com.example.mymovies.domain.models.MoviesDiscoveryDetails
import com.example.mymovies.ui.models.MoviesDiscoveryFilters

data class MainMoviesState(
    val isLoading: Boolean = false,
    val moviesDiscoveryDetails: MoviesDiscoveryDetails? = null,
    val moviesDiscoveryFilters: MoviesDiscoveryFilters = MoviesDiscoveryFilters()
)