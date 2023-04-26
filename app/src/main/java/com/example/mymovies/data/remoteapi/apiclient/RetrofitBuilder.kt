package com.example.mymovies.data.remoteapi.apiclient

import com.example.mymovies.BuildConfig
import com.example.mymovies.data.remoteapi.ApiUrlsManager
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitBuilder {

	private val networkClient: OkHttpClient = OkHttpClient.Builder()
		.addInterceptor { chain ->
			val originalRequest = chain.request()
			val originalHttpUrl = originalRequest.url()

			val modifiedUrl = originalHttpUrl.newBuilder()
				.addQueryParameter("api_key", BuildConfig.TMDB_API_KEY)
				.build()

			val modifiedRequest = originalRequest.newBuilder().url(modifiedUrl).build()
			chain.proceed(modifiedRequest)
		}
		.build()

	val retrofit: Retrofit = Retrofit.Builder()
		.baseUrl(ApiUrlsManager.theMovieDbBaseUrl)
		.client(networkClient)
		.addConverterFactory(MoshiConverterFactory.create())
		.build()
}