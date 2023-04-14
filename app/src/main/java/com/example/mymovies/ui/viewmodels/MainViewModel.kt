package com.example.mymovies.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
}