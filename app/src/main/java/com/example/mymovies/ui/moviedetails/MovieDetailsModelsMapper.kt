package com.example.mymovies.ui.moviedetails

import com.example.mymovies.domain.Movie

fun Movie.toMovieDetailsUiModel(): MovieDetailsUiModel =
    MovieDetailsUiModel(
        id,
        localTitle,
        originalTitle,
        overview,
        releaseDate,
        genres,
        voteAverage,
        posterImageRelativePath,
        backdropImageRelativePath,
        isFavourite
    )
