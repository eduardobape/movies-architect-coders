package com.example.mymovies.data.remote.services

import com.example.mymovies.data.remote.apiclient.ApiServiceFactory
import com.example.mymovies.data.remote.apiclient.create

class MoviesApi(private val apiServiceFactory: ApiServiceFactory) {

	val moviesDiscoveryApiService: MoviesDiscoveryApiService by lazy {
		apiServiceFactory.create()
	}

	val movieDetailsApiService: MovieDetailsApiService by lazy {
		apiServiceFactory.create()
	}
}