package com.example.mymovies.domain.usecases

import com.example.mymovies.domain.models.MoviesDiscoveryDetails
import com.example.mymovies.domain.repository.MoviesDiscoveryRepository

class DiscoverMoviesUseCase(private val repository: MoviesDiscoveryRepository) {

	suspend operator fun invoke(
		year: Int,
		region: String,
		language: String,
		order: String,
		page: Int
	): MoviesDiscoveryDetails = repository.discoverMoviesByYear(year, region, language, order, page)
}