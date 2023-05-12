package com.example.mymovies.data.repository

import com.example.mymovies.data.remoteapi.services.MovieDetailsApiService
import com.example.mymovies.domain.models.MovieDetails
import com.example.mymovies.domain.models.toDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface MovieDetailsRepository {

    suspend fun getMovieDetails(movieId: Int): MovieDetails
}

class MovieDetailsRepositoryImpl(private val movieDetailsApiService: MovieDetailsApiService) :
    MovieDetailsRepository {

    override suspend fun getMovieDetails(movieId: Int): MovieDetails {
        return withContext(Dispatchers.IO) {
            movieDetailsApiService.getMovieDetails(movieId).toDomainModel()
        }
    }
}