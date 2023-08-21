package com.example.mymovies.data.remote.services

import com.example.mymovies.data.remote.models.MoviesSearchRemoteResult
import com.example.mymovies.ui.models.MoviesSortFields
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesSearchApiService {

	@GET("discover/movie?include_adult=false&include_video=false")
	suspend fun findPopularMovies(
		@Query("region") region: String,
		@Query("language") language: String,
		@Query("page") page: Int,
		@Query("sort_by") sortBy: String = MoviesSortFields.POPULARITY_DESC.sortOption
	): MoviesSearchRemoteResult
}
