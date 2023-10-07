package com.example.mymovies.domain.usecases

import com.example.mymovies.data.local.models.toDomain
import com.example.mymovies.data.repositories.PaginatedMoviesRepository
import com.example.mymovies.domain.models.PaginatedMovies
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetCachedPaginatedMoviesUseCase(private val repository: PaginatedMoviesRepository) {

    operator fun invoke(): Flow<PaginatedMovies> = repository.popularPaginatedMovies.map { localPaginatedMovies ->
        localPaginatedMovies.toDomain()
    }
}
