package com.example.mymovies.data.repositories

import com.example.mymovies.data.remote.models.MovieDetailsRemote
import com.example.mymovies.data.remote.services.MovieDetailsApiService

class MovieDetailsRemoteRepository(private val movieDetailsApiService: MovieDetailsApiService) :
    MovieDetailsRepository {

    override suspend fun getMovieDetails(movieId: Int): MovieDetailsRemote =
        movieDetailsApiService.getMovieDetails(movieId)
}
