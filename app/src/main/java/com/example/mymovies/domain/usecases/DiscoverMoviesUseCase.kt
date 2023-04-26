package com.example.mymovies.domain.usecases

import com.example.mymovies.domain.models.MoviesDiscoveryDetails
import com.example.mymovies.domain.repository.MoviesDiscoveryRepository

class DiscoverMoviesUseCase(private val repository: MoviesDiscoveryRepository) {

	suspend operator fun invoke(
		releaseYear: Int,
		region: String,
		language: String,
		sortBy: String,
		page: Int
	): MoviesDiscoveryDetails = repository.discoverMoviesByYear(releaseYear, region, language, sortBy, page)
}