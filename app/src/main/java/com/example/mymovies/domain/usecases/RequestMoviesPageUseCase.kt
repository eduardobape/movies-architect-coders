package com.example.mymovies.domain.usecases

import com.example.mymovies.data.errors.Error
import com.example.mymovies.data.repositories.PaginatedMoviesRepository
import com.example.mymovies.ui.models.MoviesSearchFilters

class RequestMoviesPageUseCase(private val repository: PaginatedMoviesRepository) {

    suspend operator fun invoke(moviesSearchFilters: MoviesSearchFilters, pageToFetch: Int): Error? =
        repository.findPaginatedPopularMovies(moviesSearchFilters, pageToFetch)
}
