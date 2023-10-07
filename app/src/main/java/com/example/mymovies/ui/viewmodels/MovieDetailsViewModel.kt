package com.example.mymovies.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mymovies.data.errors.Error
import com.example.mymovies.data.errors.toError
import com.example.mymovies.domain.usecases.GetCachedMovieDetails
import com.example.mymovies.domain.usecases.RequestMovieDetailsUseCase
import com.example.mymovies.domain.usecases.SwitchMovieFavouriteUseCase
import com.example.mymovies.ui.views.MovieDetailsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MovieDetailsViewModel(
    private val movieId: Long,
    private val requestMovieDetailsUseCase: RequestMovieDetailsUseCase,
    private val getCachedMovieDetails: GetCachedMovieDetails,
    private val switchMovieFavouriteUseCase: SwitchMovieFavouriteUseCase
) : ViewModel() {

    private val _uiState: MutableStateFlow<MovieDetailsUiState> = MutableStateFlow(MovieDetailsUiState())
    val uiState: StateFlow<MovieDetailsUiState> = _uiState.asStateFlow()

    init {
        fetchMovieDetails()
        collectMovieDetails()
    }

    private fun collectMovieDetails() {
        viewModelScope.launch {
            getCachedMovieDetails(movieId)
                .catch { exception ->
                    _uiState.update {
                        it.copy(isLoading = false, error = exception.toError())
                    }
                }
                .collect { movieDetails ->
                    _uiState.update {
                        it.copy(isLoading = false, movieDetails = movieDetails, error = null)
                    }
                }
        }
    }

    private fun fetchMovieDetails() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true)
            }
            val error: Error? = requestMovieDetailsUseCase(movieId)
            error?.let {
                _uiState.update {
                    it.copy(isLoading = false, error = error)
                }
            }
        }
    }

    fun onFavouriteClicked() {
        viewModelScope.launch {
            _uiState.value.movieDetails?.let {
                _uiState.update { uiState ->
                    uiState.copy(isLoading = true)
                }
                val error: Error? = switchMovieFavouriteUseCase(it)
                error?.let {
                    _uiState.update { uiState ->
                        uiState.copy(error = error, isLoading = false)
                    }
                }
            }
        }
    }

    fun notifyErrorShown() {
        _uiState.update {
            it.copy(error = null)
        }
    }

    class Factory(
        private val movieId: Long,
        private val requestMovieDetailsUseCase: RequestMovieDetailsUseCase,
        private val getCachedMovieDetails: GetCachedMovieDetails,
        private val switchMovieFavouriteUseCase: SwitchMovieFavouriteUseCase
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            MovieDetailsViewModel(
                movieId,
                requestMovieDetailsUseCase,
                getCachedMovieDetails,
                switchMovieFavouriteUseCase
            ) as T
    }
}
