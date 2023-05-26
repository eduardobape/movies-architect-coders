package com.example.mymovies.data.repository

import com.example.mymovies.data.remoteapi.models.MovieDetailsResult
import com.example.mymovies.data.remoteapi.services.MovieDetailsApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface MovieDetailsRepository {

    suspend fun getMovieDetails(movieId: Int): MovieDetailsResult
}

class MovieDetailsRepositoryImpl(private val movieDetailsApiService: MovieDetailsApiService) :
    MovieDetailsRepository {

    override suspend fun getMovieDetails(movieId: Int): MovieDetailsResult {
        return withContext(Dispatchers.IO) {
            movieDetailsApiService.getMovieDetails(movieId)
        }
    }
}