package com.example.mymovies.data.repositories

import com.example.mymovies.data.remote.models.MoviesDiscoveryRemote
import com.example.mymovies.ui.models.MoviesDiscoveryFilters

interface MoviesDiscoveryRepository {

    suspend fun getPopularMovies(
        moviesDiscoveryFilters: MoviesDiscoveryFilters,
        pageToFetch: Int
    ): MoviesDiscoveryRemote
}