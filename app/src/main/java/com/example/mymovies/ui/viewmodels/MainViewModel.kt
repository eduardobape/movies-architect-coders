package com.example.mymovies.ui.viewmodels

import androidx.lifecycle.*
import com.example.mymovies.domain.models.MoviesDiscoveryDetails
import com.example.mymovies.domain.usecases.DiscoverMoviesUseCase
import com.example.mymovies.ui.models.MoviesDiscoveryFilters
import kotlinx.coroutines.launch

class MainViewModel(private val discoverMoviesUseCase: DiscoverMoviesUseCase) : ViewModel() {

	private val _moviesDetails: MutableLiveData<MoviesDiscoveryDetails> = MutableLiveData()
	val moviesDetails: LiveData<MoviesDiscoveryDetails>
		get() = _moviesDetails
	val moviesFilters = MoviesDiscoveryFilters()

	init {
		getMovies(moviesFilters)
	}

	fun getMovies(moviesDiscoveryFilters: MoviesDiscoveryFilters) {
		viewModelScope.launch {
			val newMoviesDetails = discoverMoviesUseCase(moviesDiscoveryFilters)
			moviesDetails.value?.let {
				val allMovies = it.movies + newMoviesDetails.movies
				newMoviesDetails.movies = allMovies
			}
			_moviesDetails.value = newMoviesDetails
			increaseMoviesPage()
		}
	}

	private fun increaseMoviesPage() {
		moviesFilters.page++
	}

	class Factory(private val discoverMoviesUseCase: DiscoverMoviesUseCase) : ViewModelProvider.Factory {
		@Suppress("UNCHECKED_CAST")
		override fun <T : ViewModel> create(modelClass: Class<T>): T = MainViewModel(discoverMoviesUseCase) as T
	}
}