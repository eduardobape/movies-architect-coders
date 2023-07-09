package com.example.mymovies.data.repository

import com.example.mymovies.data.remote.models.MoviesDiscoveryResult
import com.example.mymovies.data.remote.services.MoviesDiscoveryApiService
import com.example.mymovies.domain.repositories.MoviesDiscoveryRepository
import com.example.mymovies.ui.models.MoviesDiscoveryFilters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MoviesDiscoveryRemoteRepository(private val moviesDiscoveryApiService: MoviesDiscoveryApiService) :
    MoviesDiscoveryRepository {

    override suspend fun getPopularMovies(
        moviesDiscoveryFilters: MoviesDiscoveryFilters,
        pageToFetch: Int
    ): MoviesDiscoveryResult =
        withContext(Dispatchers.IO) {
            moviesDiscoveryApiService.getPopularMovies(
                moviesDiscoveryFilters.region,
                moviesDiscoveryFilters.language,
                pageToFetch
            )
        }
}
