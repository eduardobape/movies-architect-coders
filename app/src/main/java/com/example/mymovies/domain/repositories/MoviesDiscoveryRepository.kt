package com.example.mymovies.domain.repositories

import com.example.mymovies.data.remote.models.MoviesDiscoveryResult
import com.example.mymovies.ui.models.MoviesDiscoveryFilters

interface MoviesDiscoveryRepository {

    suspend fun getPopularMovies(
        moviesDiscoveryFilters: MoviesDiscoveryFilters,
        pageToFetch: Int
    ): MoviesDiscoveryResult
}