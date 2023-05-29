package com.example.mymovies.data.remoteapi.services

import com.example.mymovies.data.remoteapi.models.MoviesDiscoveryResult
import com.example.mymovies.ui.models.SortOptionMoviesDiscovery
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesDiscoveryApiService {

	@GET("discover/movie?include_adult=false&include_video=false")
	suspend fun getPopularMovies(
		@Query("region") region: String,
		@Query("language") language: String,
		@Query("page") page: Int,
		@Query("sort_by") sortBy: String = SortOptionMoviesDiscovery.POPULARITY_DESC.sortOption
	): MoviesDiscoveryResult
}