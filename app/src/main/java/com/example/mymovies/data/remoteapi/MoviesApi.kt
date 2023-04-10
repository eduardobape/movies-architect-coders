package com.example.mymovies.data.remoteapi

object MoviesApi {

	val moviesDiscoveryApiService: MoviesDiscoveryApiService by lazy {
		RetrofitBuilder.retrofit.create(MoviesDiscoveryApiService::class.java)
	}
}