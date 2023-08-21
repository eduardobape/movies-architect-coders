package com.example.mymovies.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mymovies.domain.usecases.GetMovieDetailsUseCase
import com.example.mymovies.ui.views.MovieDetailsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MovieDetailsViewModel(
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    movieId: Long?
) : ViewModel() {

    private val _uiState: MutableStateFlow<MovieDetailsUiState> = MutableStateFlow(MovieDetailsUiState())
    val uiState: StateFlow<MovieDetailsUiState> = _uiState.asStateFlow()

    init {
        fetchMovieDetails(movieId)
    }

    private fun fetchMovieDetails(movieId: Long?) {
        viewModelScope.launch {
            if (movieId == null) {
                _uiState.update {
                    it.copy(isError = true)
                }
            } else {
                _uiState.update {
                    it.copy(isLoading = true)
                }
                _uiState.update {
                    it.copy(isLoading = false, movieDetails = getMovieDetailsUseCase(movieId))
                }
            }
        }
    }

    class Factory(private val getMovieDetailsUseCase: GetMovieDetailsUseCase, private val movieId: Long?) :
        ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            MovieDetailsViewModel(getMovieDetailsUseCase, movieId) as T
    }
}
