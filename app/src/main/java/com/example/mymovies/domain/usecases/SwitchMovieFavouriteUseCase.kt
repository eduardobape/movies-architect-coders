package com.example.mymovies.domain.usecases

import com.example.mymovies.data.errors.Error
import com.example.mymovies.data.repositories.MovieDetailsRepository
import com.example.mymovies.domain.models.MovieDetails

class SwitchMovieFavouriteUseCase(private val repository: MovieDetailsRepository) {

    suspend operator fun invoke(movie: MovieDetails): Error? =
        repository.switchMovieFavourite(movie.id, !movie.isFavourite)
}
