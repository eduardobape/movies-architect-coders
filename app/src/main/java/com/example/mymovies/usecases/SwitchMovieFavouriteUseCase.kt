package com.example.mymovies.usecases

import com.example.mymovies.data.MoviesRepository
import com.example.mymovies.domain.Error
import com.example.mymovies.ui.moviedetails.MovieDetailsUiModel

class SwitchMovieFavouriteUseCase(private val repository: MoviesRepository) {

    suspend operator fun invoke(movie: MovieDetailsUiModel): Error? =
        repository.switchMovieFavouriteFlag(movie.id, !movie.isFavourite)
}
