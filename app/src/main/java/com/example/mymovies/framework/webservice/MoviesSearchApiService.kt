package com.example.mymovies.framework.webservice

import com.example.mymovies.domain.MoviesSortFields
import com.example.mymovies.framework.webservice.responses.MovieDetailsSearchRemoteResult
import com.example.mymovies.framework.webservice.responses.PaginatedMoviesApiResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesSearchApiService {

    @GET("discover/movie?include_adult=false&include_video=false")
    suspend fun requestPaginatedMovies(
        @Query("region") region: String,
        @Query("language") language: String,
        @Query("page") page: Int,
        @Query("sort_by") sortBy: String = MoviesSortFields.POPULARITY_DESC.sortOption
    ): PaginatedMoviesApiResponse

    @GET("movie/{movie_id}")
    suspend fun fetchMovieDetails(
        @Path("movie_id") movieId: Long,
        @Query("language") language: String
    ): MovieDetailsSearchRemoteResult
}
