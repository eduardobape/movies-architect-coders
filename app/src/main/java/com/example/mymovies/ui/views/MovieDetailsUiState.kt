package com.example.mymovies.ui.views

import com.example.mymovies.domain.models.MovieDetails

data class MovieDetailsUiState(
    val movieDetails: MovieDetails? = null,
    val loadState: MovieDetailsLoadState = MovieDetailsLoadState.Loading
)

sealed interface MovieDetailsLoadState {
    object Loading : MovieDetailsLoadState
    object Success : MovieDetailsLoadState
    class Error(val errorMessage: String) : MovieDetailsLoadState
}
