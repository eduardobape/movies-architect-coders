package com.example.mymovies.data.repository

import com.example.mymovies.data.remoteapi.models.MoviesDiscoveryResult
import com.example.mymovies.data.remoteapi.services.MoviesDiscoveryApiService
import com.example.mymovies.ui.models.MoviesDiscoveryFilters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface MoviesDiscoveryRepository {

	suspend fun getPopularMovies(moviesDiscoveryFilters: MoviesDiscoveryFilters): MoviesDiscoveryResult
}

class MoviesDiscoveryRepositoryImpl(private val moviesDiscoveryApiService: MoviesDiscoveryApiService) :
	MoviesDiscoveryRepository {
	override suspend fun getPopularMovies(moviesDiscoveryFilters: MoviesDiscoveryFilters): MoviesDiscoveryResult =
		withContext(Dispatchers.IO) {
			moviesDiscoveryApiService.getPopularMovies(
				moviesDiscoveryFilters.region,
				moviesDiscoveryFilters.language,
				moviesDiscoveryFilters.nextMoviesPageToFetch
			)
		}
}
