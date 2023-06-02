package com.example.mymovies.data.remote.services

import com.example.mymovies.data.remote.models.MovieDetailsResult
import retrofit2.http.GET
import retrofit2.http.Path

interface MovieDetailsApiService {

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(@Path("movie_id") movieId: Int): MovieDetailsResult
}