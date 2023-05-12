package com.example.mymovies.data.remoteapi.services

import com.example.mymovies.data.remoteapi.apiclient.RetrofitBuilder

object MoviesApi {

	val moviesDiscoveryApiService: MoviesDiscoveryApiService by lazy {
		RetrofitBuilder.retrofit.create(MoviesDiscoveryApiService::class.java)
	}

	val movieDetailsApiService: MovieDetailsApiService by lazy {
		RetrofitBuilder.retrofit.create(MovieDetailsApiService::class.java)
	}
}