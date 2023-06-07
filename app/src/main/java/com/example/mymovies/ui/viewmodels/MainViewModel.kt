package com.example.mymovies.ui.viewmodels


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mymovies.domain.models.MoviesDiscoveryDetails
import com.example.mymovies.domain.usecases.GetPopularMoviesUseCase
import com.example.mymovies.ui.models.MoviesDiscoveryFilters
import com.example.mymovies.ui.views.MoviesDiscoveryState
import kotlinx.coroutines.launch

class MainViewModel(private val getPopularMoviesUseCase: GetPopularMoviesUseCase) : ViewModel() {

	private val _uiState: MutableLiveData<MoviesDiscoveryState> = MutableLiveData(MoviesDiscoveryState())
	val uiState: LiveData<MoviesDiscoveryState>
		get() = _uiState

	init {
		getMovies()
	}

	fun getMovies() {
		viewModelScope.launch {
			if (areMoreMoviesToFetch()) {
				_uiState.value = _uiState.value?.copy(isLoading = true)
				val moviesFilters: MoviesDiscoveryFilters =
					_uiState.value?.moviesDiscoveryFilters ?: MoviesDiscoveryFilters()
				val newMoviesDetails: MoviesDiscoveryDetails = getPopularMoviesUseCase(moviesFilters)
				newMoviesDetails.movies = _uiState.value?.moviesDiscoveryDetails?.let { lastMoviesDiscoveryDetails ->
					lastMoviesDiscoveryDetails.movies + newMoviesDetails.movies
				} ?: newMoviesDetails.movies
				_uiState.value = _uiState.value?.copy(
					isLoading = false,
					moviesDiscoveryDetails = newMoviesDetails
				)
				increaseMoviesPage()
			}
		}
	}

	private fun areMoreMoviesToFetch(): Boolean {
		_uiState.value?.let { state ->
			state.moviesDiscoveryDetails?.let {
				return state.moviesDiscoveryFilters.nextMoviesPageToFetch <= it.pages &&
						state.moviesDiscoveryFilters.maxNumMoviesToFetch > it.movies.size
			}
		}
		return true
	}

	private fun increaseMoviesPage() {
		_uiState.value?.let {
			it.moviesDiscoveryFilters.nextMoviesPageToFetch++
		}
	}


	class Factory(private val getPopularMoviesUseCase: GetPopularMoviesUseCase) : ViewModelProvider.Factory {
		@Suppress("UNCHECKED_CAST")
		override fun <T : ViewModel> create(modelClass: Class<T>): T = MainViewModel(getPopularMoviesUseCase) as T
	}
}
