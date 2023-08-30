package com.example.mymovies.data.remote.services

class MoviesApiServices(private val apiServicesFactory: ApiServicesFactory) {

	val moviesSearchApiService: MoviesSearchApiService by lazy {
		apiServicesFactory.create()
	}
}
