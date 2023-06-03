package com.example.mymovies.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mymovies.domain.usecases.MovieDetailsUseCase
import com.example.mymovies.ui.views.MovieDetailsLoadState
import com.example.mymovies.ui.views.MovieDetailsUiState
import kotlinx.coroutines.launch

class MovieDetailsViewModel(
    private val movieDetailsUseCase: MovieDetailsUseCase,
    movieId: Int?
) : ViewModel() {

    private val _uiState: MutableLiveData<MovieDetailsUiState> =
        MutableLiveData(MovieDetailsUiState())
    val uiState: LiveData<MovieDetailsUiState>
        get() = _uiState

    init {
        getMovieDetails(movieId)
    }

    private fun getMovieDetails(movieId: Int?) {
        viewModelScope.launch {
            _uiState.value?.let { uiState ->
                if (movieId == null) {
                    _uiState.value = uiState.copy(loadState = MovieDetailsLoadState.Error("Error"))
                } else {
                    _uiState.value = uiState.copy(loadState = MovieDetailsLoadState.Loading)
                    _uiState.value = uiState.copy(
                        movieDetails = movieDetailsUseCase(movieId),
                        loadState = MovieDetailsLoadState.Success
                    )
                }
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
