package com.example.mymovies.ui.moviedetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mymovies.domain.Error
import com.example.mymovies.domain.Movie
import com.example.mymovies.framework.errors.toError
import com.example.mymovies.usecases.BuildMovieImageUrlUseCase
import com.example.mymovies.usecases.GetCachedMovieDetailsUseCase
import com.example.mymovies.usecases.RequestMovieDetailsUseCase
import com.example.mymovies.usecases.SwitchMovieFavouriteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MovieDetailsViewModel(
    private val movieId: Long,
    private val requestMovieDetailsUseCase: RequestMovieDetailsUseCase,
    private val getCachedMovieDetailsUseCase: GetCachedMovieDetailsUseCase,
    private val buildMovieImageUrlUseCase: BuildMovieImageUrlUseCase,
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
            getCachedMovieDetailsUseCase(movieId)
                .catch { exception ->
                    _uiState.update {
                        it.copy(isLoading = false, error = exception.toError())
                    }
                }
                .collect { movie: Movie ->
                    val movieDetails: MovieDetailsUiModel = movie.backdropImageRelativePath?.let {
                        val backdropImageUrl = buildMovieImageUrlUseCase(it)
                        movie.toMovieDetailsUiModel().copy(backdropImageUrl = backdropImageUrl)
                    } ?: movie.toMovieDetailsUiModel()
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
            _uiState.value.movieDetails?.let { movieDetails: MovieDetailsUiModel ->
                _uiState.update { uiState ->
                    uiState.copy(isLoading = true)
                }
                val error: Error? = switchMovieFavouriteUseCase(movieDetails)
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
        private val getCachedMovieDetailsUseCase: GetCachedMovieDetailsUseCase,
        private val buildMovieImageUrlUseCase: BuildMovieImageUrlUseCase,
        private val switchMovieFavouriteUseCase: SwitchMovieFavouriteUseCase
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            MovieDetailsViewModel(
                movieId,
                requestMovieDetailsUseCase,
                getCachedMovieDetailsUseCase,
                buildMovieImageUrlUseCase,
                switchMovieFavouriteUseCase
            ) as T
    }
}
