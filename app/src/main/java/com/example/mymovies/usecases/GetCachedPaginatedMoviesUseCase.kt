package com.example.mymovies.usecases

import com.example.mymovies.data.MoviesRepository
import com.example.mymovies.domain.PaginatedMovies
import kotlinx.coroutines.flow.Flow

class GetCachedPaginatedMoviesUseCase(private val repository: MoviesRepository) {

    operator fun invoke(): Flow<PaginatedMovies> = repository.allCachedMovies
}
