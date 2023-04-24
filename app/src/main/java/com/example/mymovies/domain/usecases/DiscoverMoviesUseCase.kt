package com.example.mymovies.domain.usecases

import com.example.mymovies.domain.models.MoviesDiscoveryDetails
import com.example.mymovies.domain.repository.MoviesDiscoveryRepository

class DiscoverMoviesUseCase(private val repository: MoviesDiscoveryRepository) {

	suspend operator fun invoke(
		year: Int,
		order: String,
		region: String,
		language: String,
		page: Int
	): MoviesDiscoveryDetails = repository.discoverMoviesByYear(year, page, order, region, language)
}