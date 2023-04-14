package com.example.mymovies.ui.viewmodels

import androidx.lifecycle.*
import com.example.mymovies.domain.models.MovieMainDetails
import com.example.mymovies.domain.usecases.DiscoverMoviesUseCase
import kotlinx.coroutines.launch

class MainViewModel(private val discoverMoviesUseCase: DiscoverMoviesUseCase) : ViewModel() {

	private val _movies: MutableLiveData<List<MovieMainDetails>> = MutableLiveData(emptyList())
	val movies: LiveData<List<MovieMainDetails>>
		get() = _movies

	init {
		getMovies()
	}

	private fun getMovies() {
		viewModelScope.launch {
			val movies: List<MovieMainDetails> = discoverMoviesUseCase()
			_movies.value = movies
		}
	}

	class Factory(private val discoverMoviesUseCase: DiscoverMoviesUseCase) : ViewModelProvider.Factory {
		@Suppress("UNCHECKED_CAST")
		override fun <T : ViewModel> create(modelClass: Class<T>): T = MainViewModel(discoverMoviesUseCase) as T
	}
}