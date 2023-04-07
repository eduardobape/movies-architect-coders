package com.example.mymovies.data.apiservices

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitBuilder {

	val retrofit: Retrofit = Retrofit.Builder()
		.baseUrl(theMovieDbBaseUrl)
		.addConverterFactory(MoshiConverterFactory.create())
		.build()
}