package com.example.mymovies.ui.viewmodels


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mymovies.domain.usecases.GetPopularMoviesUseCase
import com.example.mymovies.ui.models.MoviesDiscoveryFilters
import com.example.mymovies.ui.views.MoviesDiscoveryState
import com.example.mymovies.ui.views.MoviesLoadState
import kotlinx.coroutines.launch

class MainViewModel(private val getPopularMoviesUseCase: GetPopularMoviesUseCase) : ViewModel() {

	val moviesFilters = MoviesDiscoveryFilters()
	private val _uiState: MutableLiveData<MoviesDiscoveryState> = MutableLiveData(MoviesDiscoveryState())
	val uiState: LiveData<MoviesDiscoveryState>
		get() = _uiState

	init {
		getMovies(moviesFilters)
	}

	fun getMovies(moviesFilters: MoviesDiscoveryFilters) {
		viewModelScope.launch {
			_uiState.value?.let { currentUiState ->
				if (!areMoreMoviesToFetch()) {
					_uiState.value = currentUiState.copy(moviesLoadState = MoviesLoadState.ExhaustedPagination)
				} else {
					_uiState.value = currentUiState.copy(moviesLoadState = MoviesLoadState.Loading)
					val newMoviesDetails = getPopularMoviesUseCase(moviesFilters)
					// Add the new movies fetched to the existing ones
					newMoviesDetails.movies = currentUiState.moviesDiscoveryDetails?.let {
						it.movies + newMoviesDetails.movies
					} ?: newMoviesDetails.movies
					_uiState.value = currentUiState.copy(
						moviesDiscoveryDetails = newMoviesDetails,
						moviesLoadState = MoviesLoadState.Success
					)
				}
			}
			increaseMoviesPage()
		}
	}

	private fun areMoreMoviesToFetch(): Boolean {
		_uiState.value?.let { state ->
			state.moviesDiscoveryDetails?.let {
				return moviesFilters.nextMoviesPageToFetch <= it.pages &&
						moviesFilters.maxNumMoviesToFetch > it.movies.size
			}
		}
		return true
	}

	private fun increaseMoviesPage() {
		moviesFilters.nextMoviesPageToFetch++
	}

	fun getState(): MoviesLoadState = uiState.value?.moviesLoadState ?: MoviesLoadState.Loading

	class Factory(private val getPopularMoviesUseCase: GetPopularMoviesUseCase) : ViewModelProvider.Factory {
		@Suppress("UNCHECKED_CAST")
		override fun <T : ViewModel> create(modelClass: Class<T>): T = MainViewModel(getPopularMoviesUseCase) as T
	}
}
