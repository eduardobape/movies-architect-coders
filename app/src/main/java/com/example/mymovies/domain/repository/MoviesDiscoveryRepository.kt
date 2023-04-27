package com.example.mymovies.domain.repository

import com.example.mymovies.domain.models.MoviesDiscoveryDetails
import com.example.mymovies.ui.models.MoviesDiscoveryFilters

interface MoviesDiscoveryRepository {

	suspend fun discoverMoviesByYear(moviesDiscoveryFilters: MoviesDiscoveryFilters): MoviesDiscoveryDetails
}