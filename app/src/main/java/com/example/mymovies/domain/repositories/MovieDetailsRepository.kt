package com.example.mymovies.domain.repositories

import com.example.mymovies.data.remote.models.MovieDetailsResult

interface MovieDetailsRepository {

    suspend fun getMovieDetails(movieId: Int): MovieDetailsResult
}