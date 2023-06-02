package com.example.mymovies.data.remote.services

class MoviesApi(private val apiServiceFactory: ApiServiceFactory) {

	val moviesDiscoveryApiService: MoviesDiscoveryApiService by lazy {
		apiServiceFactory.create()
	}

	val movieDetailsApiService: MovieDetailsApiService by lazy {
		apiServiceFactory.create()
	}
}