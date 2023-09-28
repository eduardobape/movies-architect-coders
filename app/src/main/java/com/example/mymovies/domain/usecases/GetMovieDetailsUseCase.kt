package com.example.mymovies.domain.usecases

import com.example.mymovies.data.errors.Error
import com.example.mymovies.data.repositories.MovieDetailsRepository
import com.example.mymovies.domain.models.MovieDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetMovieDetailsUseCase(private val repository: MovieDetailsRepository, private val movieId: Long) {

    val movieDetailsWithGenres: Flow<MovieDetails> = repository.getMovieDetailsWithGenres(movieId).map {
        MovieDetails(
            it.movie.id,
            it.movie.title,
            it.movie.originalTitle,
            it.movie.overview,
            it.movie.releaseDate,
            it.genres.map { movieGenre -> movieGenre.name },
            it.movie.voteAverage,
            it.movie.posterPathUrl,
            it.movie.backdropPathUrl,
            it.movie.isFavourite
        )
    }

    suspend operator fun invoke(): Error? = repository.findMovieDetailsById(movieId)
}
