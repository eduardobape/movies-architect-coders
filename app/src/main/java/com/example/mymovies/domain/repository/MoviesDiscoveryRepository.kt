package com.example.mymovies.domain.repository

import com.example.mymovies.domain.models.MovieMainDetails

interface MoviesDiscoveryRepository {

	suspend fun discoverMoviesByYear(
		releaseYear: Int,
		page: Int,
		sortBy: String,
		region: String,
		language: String
	): List<MovieMainDetails>
}