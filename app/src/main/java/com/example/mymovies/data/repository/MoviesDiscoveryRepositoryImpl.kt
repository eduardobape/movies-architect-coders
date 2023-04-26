package com.example.mymovies.data.repository

import com.example.mymovies.data.remoteapi.services.MoviesDiscoveryApiService
import com.example.mymovies.domain.models.MoviesDiscoveryDetails
import com.example.mymovies.domain.models.asDomainModel
import com.example.mymovies.domain.repository.MoviesDiscoveryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MoviesDiscoveryRepositoryImpl(private val moviesDiscoveryApiService: MoviesDiscoveryApiService) :
	MoviesDiscoveryRepository {

	override suspend fun discoverMoviesByYear(
		releaseYear: Int,
		region: String,
		language: String,
		sortBy: String,
		page: Int
	): MoviesDiscoveryDetails = withContext(Dispatchers.IO) {
		moviesDiscoveryApiService.discoverMoviesByYear(releaseYear, region, language, sortBy, page).asDomainModel()
	}
}