package com.example.mymovies.ui.viewmodels

import androidx.lifecycle.*
import com.example.mymovies.domain.models.MoviesDiscoveryDetails
import com.example.mymovies.domain.usecases.DiscoverMoviesUseCase
import kotlinx.coroutines.launch

class MainViewModel(private val discoverMoviesUseCase: DiscoverMoviesUseCase) : ViewModel() {

	private val _moviesDetails: MutableLiveData<MoviesDiscoveryDetails> = MutableLiveData()
	val moviesDetails: LiveData<MoviesDiscoveryDetails>
		get() = _moviesDetails

	fun getMovies(year: Int, region: String, language: String, order: String, page: Int) {
		viewModelScope.launch {
			val moviesDetails = discoverMoviesUseCase(year, region, language, order, page)
			_moviesDetails.value = moviesDetails
		}
	}

	class Factory(private val discoverMoviesUseCase: DiscoverMoviesUseCase) : ViewModelProvider.Factory {
		@Suppress("UNCHECKED_CAST")
		override fun <T : ViewModel> create(modelClass: Class<T>): T = MainViewModel(discoverMoviesUseCase) as T
	}
}