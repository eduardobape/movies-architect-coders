package com.example.mymovies.data.webservice

import com.example.mymovies.data.webservice.responses.MovieDetailsSearchRemoteResult
import com.example.mymovies.data.webservice.responses.PaginatedMoviesApiResponse
import com.example.mymovies.domain.MoviesSortFields
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
