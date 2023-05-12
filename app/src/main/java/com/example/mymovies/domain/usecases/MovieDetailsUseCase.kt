package com.example.mymovies.domain.usecases

import com.example.mymovies.data.repository.MovieDetailsRepository
import com.example.mymovies.domain.models.MovieDetails

class MovieDetailsUseCase(private val repository: MovieDetailsRepository) {

    suspend operator fun invoke(movieId: Int): MovieDetails = repository.getMovieDetails(movieId)
}