package com.example.mymovies.data.remoteapi.services

import com.example.mymovies.data.remoteapi.RetrofitBuilder

object MoviesApi {

	val moviesDiscoveryApiService: MoviesDiscoveryApiService by lazy {
		RetrofitBuilder.retrofit.create(MoviesDiscoveryApiService::class.java)
	}
}