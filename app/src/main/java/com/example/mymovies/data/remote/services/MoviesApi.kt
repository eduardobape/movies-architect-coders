package com.example.mymovies.data.remote.services

import com.example.mymovies.data.remote.client.ApiServiceFactory
import com.example.mymovies.data.remote.client.create

class MoviesApi(private val apiServiceFactory: ApiServiceFactory) {

	val moviesDiscoveryApiService: MoviesDiscoveryApiService by lazy {
		apiServiceFactory.create()
	}

	val movieDetailsApiService: MovieDetailsApiService by lazy {
		apiServiceFactory.create()
	}
}