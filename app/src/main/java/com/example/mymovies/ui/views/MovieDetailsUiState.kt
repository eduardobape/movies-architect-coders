package com.example.mymovies.ui.views

import com.example.mymovies.data.errors.Error
import com.example.mymovies.domain.models.MovieDetails

data class MovieDetailsUiState(
    val isLoading: Boolean = false,
    val movieDetails: MovieDetails? = null,
    val error: Error? = null
)
