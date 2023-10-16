package com.example.mymovies.usecases

import com.example.mymovies.data.MoviesRepository
import com.example.mymovies.domain.Error

class RequestMovieDetailsUseCase(private val repository: MoviesRepository) {

    suspend operator fun invoke(movieId: Long): Error? = repository.requestMovieDetails(movieId)
}
