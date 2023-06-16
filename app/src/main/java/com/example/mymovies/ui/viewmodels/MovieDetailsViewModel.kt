package com.example.mymovies.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mymovies.domain.usecases.MovieDetailsUseCase
import com.example.mymovies.ui.views.MovieDetailsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MovieDetailsViewModel(
    private val movieDetailsUseCase: MovieDetailsUseCase,
    movieId: Int?
) : ViewModel() {

    private val _uiState: MutableStateFlow<MovieDetailsState> = MutableStateFlow(MovieDetailsState.Loading)
    val uiState: StateFlow<MovieDetailsState> = _uiState.asStateFlow()

    init {
        fetchMovieDetails(movieId)
    }

    private fun fetchMovieDetails(movieId: Int?) {
        viewModelScope.launch {
            if (movieId == null) {
                _uiState.value = MovieDetailsState.Error
            } else {
                _uiState.value = MovieDetailsState.Loading
                _uiState.value = MovieDetailsState.Success(movieDetailsUseCase(movieId))
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
