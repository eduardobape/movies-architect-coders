package com.example.mymovies.data.repositories

import com.example.mymovies.data.remote.services.RetrofitApiServices
import com.example.mymovies.data.remote.models.MovieDetailsSearchRemoteResult
import com.example.mymovies.data.remote.services.MoviesApiServices

class MovieDetailsRepository {

    private val movieDetailsApiService = MoviesApiServices(RetrofitApiServices).movieDetailsApiService

    suspend fun getMovieDetails(movieId: Long): MovieDetailsSearchRemoteResult =
        movieDetailsApiService.getMovieDetails(movieId)
}
