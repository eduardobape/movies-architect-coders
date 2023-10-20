package com.example.mymovies.usescases

import com.example.mymovies.data.MoviesRepository
import com.example.mymovies.domain.Error

class SwitchMovieFavouriteUseCase(private val repository: MoviesRepository) {

    suspend operator fun invoke(movieId: Long, isMovieFavouriteFlag: Boolean): Error? =
        repository.switchMovieFavouriteFlag(movieId, !isMovieFavouriteFlag)
}
