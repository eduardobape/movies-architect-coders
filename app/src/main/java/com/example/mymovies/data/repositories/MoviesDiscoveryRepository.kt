package com.example.mymovies.data.repositories

import com.example.mymovies.data.remote.client.RetrofitServiceBuilder
import com.example.mymovies.data.remote.models.MoviesDiscoveryRemote
import com.example.mymovies.data.remote.services.MoviesApi
import com.example.mymovies.data.remote.services.MoviesDiscoveryApiService
import com.example.mymovies.ui.models.MoviesDiscoveryFilters

class MoviesDiscoveryRepository {

    private val moviesDiscoveryApiService: MoviesDiscoveryApiService =
        MoviesApi(RetrofitServiceBuilder).moviesDiscoveryApiService

    suspend fun getPopularMovies(
        moviesDiscoveryFilters: MoviesDiscoveryFilters,
        pageToFetch: Int
    ): MoviesDiscoveryRemote =
        moviesDiscoveryApiService.getPopularMovies(
            moviesDiscoveryFilters.region,
            moviesDiscoveryFilters.language,
            pageToFetch
        )
}
