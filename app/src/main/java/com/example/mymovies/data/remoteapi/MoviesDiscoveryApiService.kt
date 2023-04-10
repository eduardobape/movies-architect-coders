package com.example.mymovies.data.remoteapi

import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesDiscoveryApiService {

	@GET("/discover/movie")
	suspend fun discoverMoviesByYear(
		@Query("year") year: Int,
		@Query("page") page: Int,
		@Query("sort_by") sortBy: String = "release_date.desc",
		@Query("region") region: String = "ES",
		@Query("language") language: String = "es-ES"
	): List<MoviesDiscoveryResult>
}