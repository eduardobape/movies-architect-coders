package com.example.mymovies.ui.views

import com.example.mymovies.domain.models.MoviesDiscoveryDetails

data class MoviesDiscoveryState(
	val moviesDiscoveryDetails: MoviesDiscoveryDetails? = null,
	val moviesLoadState: MoviesLoadState = MoviesLoadState.Loading
)

sealed interface MoviesLoadState {
	object Success : MoviesLoadState
	object Loading : MoviesLoadState
	object ExhaustedPagination : MoviesLoadState
	class Error(val errorMessage: String = "") : MoviesLoadState
}
