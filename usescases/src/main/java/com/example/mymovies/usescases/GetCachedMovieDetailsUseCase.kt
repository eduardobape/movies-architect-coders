package com.example.mymovies.usescases

import com.example.mymovies.data.MoviesRepository
import com.example.mymovies.domain.Movie
import kotlinx.coroutines.flow.Flow

class GetCachedMovieDetailsUseCase(private val repository: MoviesRepository) {

    operator fun invoke(movieId: Long): Flow<Movie> = repository.getMovieDetailsWithGenres(movieId)
}
