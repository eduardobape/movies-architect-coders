package com.example.mymovies.domain.usecases

import com.example.mymovies.domain.models.MovieMainDetails
import com.example.mymovies.domain.repository.MoviesDiscoveryRepository

class DiscoverMoviesUseCase(private val repository: MoviesDiscoveryRepository) {

	suspend operator fun invoke(): List<MovieMainDetails> = repository.discoverMoviesByYear(
		2023, 1, "release.desc", "ES", "es-ES"
	)
}
