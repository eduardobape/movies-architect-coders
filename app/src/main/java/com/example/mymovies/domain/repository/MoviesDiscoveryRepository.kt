package com.example.mymovies.domain.repository

import com.example.mymovies.domain.models.MoviesDiscoveryDetails

interface MoviesDiscoveryRepository {

	suspend fun discoverMoviesByYear(
		releaseYear: Int,
		region: String,
		language: String,
		sortBy: String,
		page: Int
	): MoviesDiscoveryDetails
}