package com.example.mymovies.ui.viewmodels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mymovies.domain.models.MoviesDiscoveryDetails
import com.example.mymovies.domain.usecases.GetPopularMoviesUseCase
import com.example.mymovies.ui.models.MoviesDiscoveryFilters
import com.example.mymovies.ui.views.PaginatedMoviesMainUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val getPopularMoviesUseCase: GetPopularMoviesUseCase) : ViewModel() {

    companion object {
        private const val MAX_MOVIES_TO_FETCH = 100
    }

    private val _uiState: MutableStateFlow<PaginatedMoviesMainUiState> = MutableStateFlow(PaginatedMoviesMainUiState())
    val uiState: StateFlow<PaginatedMoviesMainUiState> = _uiState.asStateFlow()
    private var isFirstLoadOfMovies: Boolean = true

    init {
        getMovies()
    }

    fun getMovies() {
        viewModelScope.launch {
            if (isFirstLoadOfMovies || areMoreMoviesToFetch()) {
                _uiState.value = uiState.value.copy(isLoading = true)
                val moviesFilters: MoviesDiscoveryFilters = uiState.value.moviesDiscoveryFilters
                val newMoviesDetails: MoviesDiscoveryDetails =
                    getPopularMoviesUseCase(moviesFilters, uiState.value.moviesDiscoveryDetails.page + 1)
                _uiState.value = uiState.value.copy(
                    isLoading = false,
                    moviesDiscoveryDetails = newMoviesDetails.copy(
                        movies = uiState.value.moviesDiscoveryDetails.movies + newMoviesDetails.movies
                    )
                )
                isFirstLoadOfMovies = false
            }
        }
    }

    private fun areMoreMoviesToFetch(): Boolean {
        return with(uiState.value.moviesDiscoveryDetails) {
            page < pages && MAX_MOVIES_TO_FETCH > movies.size
        }
    }


    class Factory(private val getPopularMoviesUseCase: GetPopularMoviesUseCase) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T = MainViewModel(getPopularMoviesUseCase) as T
    }
}
