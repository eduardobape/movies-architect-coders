package com.example.mymovies.data.repository

import com.example.mymovies.data.remote.models.MoviesDiscoveryResult
import com.example.mymovies.data.remote.services.MoviesDiscoveryApiService
import com.example.mymovies.domain.repositories.MoviesDiscoveryRepository
import com.example.mymovies.ui.models.MoviesDiscoveryFilters

class MoviesDiscoveryRemoteRepository(private val moviesDiscoveryApiService: MoviesDiscoveryApiService) :
    MoviesDiscoveryRepository {

    override suspend fun getPopularMovies(
        moviesDiscoveryFilters: MoviesDiscoveryFilters,
        pageToFetch: Int
    ): MoviesDiscoveryResult =
        moviesDiscoveryApiService.getPopularMovies(
            moviesDiscoveryFilters.region,
            moviesDiscoveryFilters.language,
            pageToFetch
        )
}
