package com.example.mymovies.data.repository

import com.example.mymovies.domain.models.asDomainModel
import com.example.mymovies.data.remoteapi.services.MoviesDiscoveryApiService
import com.example.mymovies.domain.models.MovieMainDetails
import com.example.mymovies.domain.repository.MoviesDiscoveryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MoviesDiscoveryRepositoryImpl(private val moviesDiscoveryApiService: MoviesDiscoveryApiService) :
	MoviesDiscoveryRepository {

	override suspend fun discoverMoviesByYear(
		releaseYear: Int,
		page: Int,
		sortBy: String,
		region: String,
		language: String
	): List<MovieMainDetails> = withContext(Dispatchers.IO) {
		moviesDiscoveryApiService.discoverMoviesByYear(releaseYear, page, sortBy, region, language).asDomainModel()
	}
}