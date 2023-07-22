package com.example.mymovies.data.repositories

import com.example.mymovies.data.remote.models.MovieDetailsRemote

interface MovieDetailsRepository {

    suspend fun getMovieDetails(movieId: Int): MovieDetailsRemote
}