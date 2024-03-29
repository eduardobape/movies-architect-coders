package com.example.mymovies.usescases

import com.example.mymovies.data.MoviesRepository
import com.example.mymovies.domain.MovieImageSize

class BuildMovieImageUrlUseCase(
    private val moviesRepository: MoviesRepository, private val imageSize: MovieImageSize
) {

    operator fun invoke(movieImageRelativePathUrl: String): String =
        moviesRepository.buildMovieImageUrl(movieImageRelativePathUrl, imageSize)
}
