package com.example.mymovies.data.remoteapi.services

import com.example.mymovies.data.remoteapi.models.MoviesDiscoveryResult
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesDiscoveryApiService {

	@GET("discover/movie")
	suspend fun discoverMoviesByYear(
		@Query("year") year: Int,
		@Query("region") region: String,
		@Query("language") language: String,
		@Query("sort_by") sortBy: String,
		@Query("page") page: Int
	): MoviesDiscoveryResult
}