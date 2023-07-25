package com.example.mymovies.data.repositories

import com.example.mymovies.data.remote.client.RetrofitServiceBuilder
import com.example.mymovies.data.remote.models.MovieDetailsRemote
import com.example.mymovies.data.remote.services.MoviesApi

class MovieDetailsRepository {

    private val movieDetailsApiService = MoviesApi(RetrofitServiceBuilder).movieDetailsApiService

    suspend fun getMovieDetails(movieId: Int): MovieDetailsRemote =
        movieDetailsApiService.getMovieDetails(movieId)
}
