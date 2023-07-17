package com.example.mymovies.data.repository

import com.example.mymovies.data.remote.models.MovieDetailsResult
import com.example.mymovies.data.remote.services.MovieDetailsApiService
import com.example.mymovies.domain.repositories.MovieDetailsRepository

class MovieDetailsRemoteRepository(private val movieDetailsApiService: MovieDetailsApiService) :
    MovieDetailsRepository {

    override suspend fun getMovieDetails(movieId: Int): MovieDetailsResult =
        movieDetailsApiService.getMovieDetails(movieId)
}
