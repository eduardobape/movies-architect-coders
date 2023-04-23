package com.example.mymovies.ui.viewmodels

import androidx.lifecycle.*
import com.example.mymovies.domain.models.MovieMainDetails
import com.example.mymovies.domain.usecases.DiscoverMoviesUseCase
import kotlinx.coroutines.launch

class MainViewModel(private val discoverMoviesUseCase: DiscoverMoviesUseCase) : ViewModel() {

	private val _movies: MutableLiveData<List<MovieMainDetails>> = MutableLiveData(emptyList())
	val movies: LiveData<List<MovieMainDetails>>
		get() = _movies

	fun getMovies(year: Int, region: String, language: String, order: String, page: Int) {
		viewModelScope.launch {
			val movies: List<MovieMainDetails> = discoverMoviesUseCase(year, order, region, language, page)
			_movies.value = movies
		}
	}

	class Factory(private val discoverMoviesUseCase: DiscoverMoviesUseCase) : ViewModelProvider.Factory {
		@Suppress("UNCHECKED_CAST")
		override fun <T : ViewModel> create(modelClass: Class<T>): T = MainViewModel(discoverMoviesUseCase) as T
	}
}