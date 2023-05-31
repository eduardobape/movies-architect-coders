package com.example.mymovies.data.remoteapi.services

import com.example.mymovies.data.remoteapi.apiclient.RetrofitBuilder
import retrofit2.create

object MoviesApi {

	val moviesDiscoveryApiService: MoviesDiscoveryApiService by lazy {
		RetrofitBuilder.retrofit.create()
	}

	val movieDetailsApiService: MovieDetailsApiService by lazy {
		RetrofitBuilder.retrofit.create()
	}
}