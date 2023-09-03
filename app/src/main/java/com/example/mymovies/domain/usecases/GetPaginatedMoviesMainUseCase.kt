package com.example.mymovies.domain.usecases

import com.example.mymovies.data.repositories.PaginatedMoviesRepository
import com.example.mymovies.domain.models.PaginatedMovies
import com.example.mymovies.data.local.models.toDomain
import com.example.mymovies.ui.models.MoviesSearchFilters
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetPaginatedMoviesMainUseCase(private val repository: PaginatedMoviesRepository) {

    val popularMoviesFlow: Flow<PaginatedMovies> = repository.popularPaginatedMovies.map { localPaginatedMovies ->
        localPaginatedMovies.toDomain()
    }

    suspend operator fun invoke(moviesSearchFilters: MoviesSearchFilters, pageToFetch: Int) {
        repository.findPaginatedPopularMovies(moviesSearchFilters, pageToFetch)
    }
}
