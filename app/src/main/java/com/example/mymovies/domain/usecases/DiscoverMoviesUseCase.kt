package com.example.mymovies.domain.usecases

import com.example.mymovies.domain.models.MovieMainDetails
import com.example.mymovies.domain.repository.MoviesDiscoveryRepository

class DiscoverMoviesUseCase(private val repository: MoviesDiscoveryRepository) {

	suspend operator fun invoke(
		year: Int,
		order: String,
		region: String,
		language: String,
		page: Int
	): List<MovieMainDetails> = repository.discoverMoviesByYear(year, page, order, region, language)
}
