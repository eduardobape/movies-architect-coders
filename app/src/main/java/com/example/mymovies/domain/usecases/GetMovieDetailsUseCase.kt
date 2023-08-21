package com.example.mymovies.domain.usecases

import com.example.mymovies.data.repositories.MovieDetailsRepository
import com.example.mymovies.domain.models.MovieDetails
import com.example.mymovies.data.remote.models.toDomainModel

class GetMovieDetailsUseCase(private val repository: MovieDetailsRepository) {

    suspend operator fun invoke(movieId: Long): MovieDetails =
        repository.getMovieDetails(movieId).toDomainModel()
}
