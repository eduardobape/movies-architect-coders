package com.example.mymovies.ui.moviedetails

import com.example.mymovies.domain.Error
import com.example.mymovies.domain.MovieGenre

data class MovieDetailsUiState(
    val isLoading: Boolean = false,
    val movieDetails: MovieDetailsUiModel? = null,
    val error: Error? = null
)

data class MovieDetailsUiModel(
    val id: Long,
    val localTitle: String,
    val originalTitle: String,
    val overview: String?,
    val releaseDate: String,
    val genres: List<MovieGenre>,
    val voteAverage: Float,
    val posterImageUrl: String?,
    val backdropImageUrl: String?,
    val isFavourite: Boolean
)

fun MovieDetailsUiModel.hasBackdropImageUrl(): Boolean =
    backdropImageUrl != null
