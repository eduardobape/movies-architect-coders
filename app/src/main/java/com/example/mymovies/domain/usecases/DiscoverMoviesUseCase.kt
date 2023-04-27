package com.example.mymovies.domain.usecases

import com.example.mymovies.domain.models.MoviesDiscoveryDetails
import com.example.mymovies.domain.repository.MoviesDiscoveryRepository
import com.example.mymovies.ui.models.MoviesDiscoveryFilters

class DiscoverMoviesUseCase(private val repository: MoviesDiscoveryRepository) {

	suspend operator fun invoke(moviesDiscoveryFilters: MoviesDiscoveryFilters): MoviesDiscoveryDetails =
		repository.discoverMoviesByYear(moviesDiscoveryFilters)
}