package com.example.mymovies.ui.views

import com.example.mymovies.domain.models.MovieDetails

sealed interface MovieDetailsState {
	object Loading : MovieDetailsState
	class Success(val movieDetails: MovieDetails) : MovieDetailsState
	object Error : MovieDetailsState
}
