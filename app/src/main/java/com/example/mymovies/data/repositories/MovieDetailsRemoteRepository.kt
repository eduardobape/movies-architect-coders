package com.example.mymovies.data.repositories

import com.example.mymovies.data.remote.models.MovieDetailsResult
import com.example.mymovies.data.remote.services.MovieDetailsApiService

class MovieDetailsRemoteRepository(private val movieDetailsApiService: MovieDetailsApiService) :
    MovieDetailsRepository {

    override suspend fun getMovieDetails(movieId: Int): MovieDetailsResult =
        movieDetailsApiService.getMovieDetails(movieId)
}
