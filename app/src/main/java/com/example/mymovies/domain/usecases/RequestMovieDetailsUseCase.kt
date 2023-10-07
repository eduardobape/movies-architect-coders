package com.example.mymovies.domain.usecases

import com.example.mymovies.data.errors.Error
import com.example.mymovies.data.repositories.MovieDetailsRepository

class RequestMovieDetailsUseCase(private val repository: MovieDetailsRepository) {

    suspend operator fun invoke(movieId: Long): Error? = repository.findMovieDetailsById(movieId)
}
