package com.example.mymovies.ui.viewmodels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mymovies.domain.models.MoviesDiscoveryDetails
import com.example.mymovies.domain.usecases.GetPopularMoviesUseCase
import com.example.mymovies.ui.models.MoviesDiscoveryFilters
import com.example.mymovies.ui.views.MoviesDiscoveryState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val getPopularMoviesUseCase: GetPopularMoviesUseCase) : ViewModel() {

	private val _uiState: MutableStateFlow<MoviesDiscoveryState> = MutableStateFlow(MoviesDiscoveryState())
	val uiState: StateFlow<MoviesDiscoveryState> = _uiState.asStateFlow()

	init {
		getMovies()
	}

	fun getMovies() {
		viewModelScope.launch {
			if (areMoreMoviesToFetch()) {
				_uiState.value = _uiState.value.copy(isLoading = true)
				val moviesFilters: MoviesDiscoveryFilters = _uiState.value.moviesDiscoveryFilters
				val newMoviesDetails: MoviesDiscoveryDetails = getPopularMoviesUseCase(moviesFilters)
				newMoviesDetails.movies = _uiState.value.moviesDiscoveryDetails?.let { lastMoviesDiscoveryDetails ->
					lastMoviesDiscoveryDetails.movies + newMoviesDetails.movies
				} ?: newMoviesDetails.movies
				_uiState.value = _uiState.value.copy(
					isLoading = false,
					moviesDiscoveryDetails = newMoviesDetails
				)
				increaseMoviesPage()
			}
		}
	}

	private fun areMoreMoviesToFetch(): Boolean {
		_uiState.value.moviesDiscoveryDetails?.let {
			return _uiState.value.moviesDiscoveryFilters.nextMoviesPageToFetch <= it.pages &&
					_uiState.value.moviesDiscoveryFilters.maxNumMoviesToFetch > it.movies.size
		}
		return true
	}

	private fun increaseMoviesPage() {
		_uiState.value.moviesDiscoveryFilters.nextMoviesPageToFetch++
	}


	class Factory(private val getPopularMoviesUseCase: GetPopularMoviesUseCase) : ViewModelProvider.Factory {
		@Suppress("UNCHECKED_CAST")
		override fun <T : ViewModel> create(modelClass: Class<T>): T = MainViewModel(getPopularMoviesUseCase) as T
	}
}
