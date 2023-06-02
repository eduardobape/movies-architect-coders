package com.example.mymovies.data.remoteapi.services

import com.example.mymovies.data.remoteapi.apiclient.ApiServiceFactory
import com.example.mymovies.data.remoteapi.apiclient.create

class MoviesApi(private val apiServiceFactory: ApiServiceFactory) {

	val moviesDiscoveryApiService: MoviesDiscoveryApiService by lazy {
		apiServiceFactory.create()
	}

	val movieDetailsApiService: MovieDetailsApiService by lazy {
		apiServiceFactory.create()
	}
}