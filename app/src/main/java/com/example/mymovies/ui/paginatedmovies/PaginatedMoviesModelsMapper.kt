package com.example.mymovies.ui.paginatedmovies

import com.example.mymovies.domain.Movie

fun Movie.toPaginatedMovieUiModel(): PaginatedMovieUiModel =
    PaginatedMovieUiModel(
        id,
        posterImageRelativePath,
        localTitle,
        isFavourite
    )
