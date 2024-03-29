package com.example.mymovies.usescases

import com.example.mymovies.data.MoviesRepository
import com.example.mymovies.domain.Error
import com.example.mymovies.domain.MoviesSearchFilters

class RequestMoviesPageUseCase(private val repository: MoviesRepository) {

    suspend operator fun invoke(moviesSearchFilters: MoviesSearchFilters, pageToFetch: Int): Error? =
        repository.requestPaginatedMovies(moviesSearchFilters, pageToFetch)
}
