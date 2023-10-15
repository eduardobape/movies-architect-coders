package com.example.mymovies.data.datasources

import com.example.mymovies.domain.ItemsPaginationInfo
import com.example.mymovies.domain.Movie
import com.example.mymovies.domain.PaginatedMovies
import kotlinx.coroutines.flow.Flow

interface MoviesLocalDataSource {

    val allCachedMovies: Flow<List<Movie>>
    suspend fun savePaginatedMovies(paginatedMovies: PaginatedMovies)
    suspend fun getMoviesPaginationInfo(): ItemsPaginationInfo?
    suspend fun saveMovieDetails(movie: Movie)
    fun getMovieWithGenres(movieId: Long): Flow<Movie>
    suspend fun switchMovieFavouriteFlag(movieId: Long, toFavourite: Boolean)
    suspend fun isMovieFlaggedAsFavourite(movieId: Long): Boolean
}
