package com.example.mymovies.domain.usecases

import com.example.mymovies.data.repository.MoviesDiscoveryRepository
import com.example.mymovies.domain.models.MoviesDiscoveryDetails
import com.example.mymovies.domain.models.toDomainModel
import com.example.mymovies.ui.models.MoviesDiscoveryFilters

class GetPopularMoviesUseCase(private val repository: MoviesDiscoveryRepository) {

    suspend operator fun invoke(
        moviesDiscoveryFilters: MoviesDiscoveryFilters,
        pageToFetch: Int
    ): MoviesDiscoveryDetails =
        repository.getPopularMovies(moviesDiscoveryFilters, pageToFetch).toDomainModel()
}
