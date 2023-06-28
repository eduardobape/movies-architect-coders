package com.example.mymovies.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mymovies.domain.usecases.MovieDetailsUseCase
import com.example.mymovies.ui.views.MovieDetailsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MovieDetailsViewModel(
    private val movieDetailsUseCase: MovieDetailsUseCase,
    movieId: Int?
) : ViewModel() {

    private val _uiState: MutableStateFlow<MovieDetailsUiState> = MutableStateFlow(MovieDetailsUiState())
    val uiState: StateFlow<MovieDetailsUiState> = _uiState.asStateFlow()

    init {
        fetchMovieDetails(movieId)
    }

    private fun fetchMovieDetails(movieId: Int?) {
        viewModelScope.launch {
            _uiState.value = uiState.value.copy(isLoading = true, isError = false)
            if (movieId == null) {
                _uiState.value = uiState.value.copy(isLoading = false, movieDetails = null, isError = true)
            } else {
                _uiState.value = uiState.value.copy(
                    isLoading = false, movieDetails = movieDetailsUseCase(movieId), isError = false
                )
            }
        }
    }

    class Factory(private val movieDetailsUseCase: MovieDetailsUseCase, private val movieId: Int?) :
        ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            MovieDetailsViewModel(movieDetailsUseCase, movieId) as T
    }
}
