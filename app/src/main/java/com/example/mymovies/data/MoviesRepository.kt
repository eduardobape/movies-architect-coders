package com.example.mymovies.data

import com.example.mymovies.data.datasources.MoviesLocalDataSource
import com.example.mymovies.data.datasources.MoviesRemoteDataSource
import com.example.mymovies.domain.Error
import com.example.mymovies.domain.ItemsPaginationInfo
import com.example.mymovies.domain.Movie
import com.example.mymovies.domain.MovieImageSize
import com.example.mymovies.domain.MoviesSearchFilters
import com.example.mymovies.domain.PaginatedMovies
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MoviesRepository(
    private val moviesRemoteDataSource: MoviesRemoteDataSource,
    private val moviesLocalDataSource: MoviesLocalDataSource
) {
    val allCachedMovies: Flow<PaginatedMovies> = moviesLocalDataSource.allCachedMovies
        .map { movies ->
            val moviesPaginationInfo: ItemsPaginationInfo? = moviesLocalDataSource.getMoviesPaginationInfo()
            PaginatedMovies(
                movies,
                ItemsPaginationInfo(
                    moviesPaginationInfo?.lastPageLoaded ?: 0,
                    moviesPaginationInfo?.totalPages ?: 0,
                    moviesPaginationInfo?.totalItems ?: 0
                )
            )
        }

    suspend fun requestPaginatedMovies(
        moviesSearchFilters: MoviesSearchFilters,
        pageToFetch: Int
    ): Error? {
        moviesRemoteDataSource.requestPaginatedMovies(moviesSearchFilters, pageToFetch)
        return moviesRemoteDataSource.requestPaginatedMovies(moviesSearchFilters, pageToFetch).fold(
            ifLeft = { error: Error -> error },
            ifRight = { moviesLocalDataSource.savePaginatedMovies(it) }
        )
    }

    fun getMovieDetailsWithGenres(movieId: Long): Flow<Movie> =
        moviesLocalDataSource.getMovieWithGenres(movieId)

    suspend fun requestMovieDetails(movieId: Long): Error? {
        return moviesRemoteDataSource.requestMovieDetails(movieId).fold(
            ifLeft = { error: Error -> error },
            ifRight = { movie ->
                moviesLocalDataSource.isMovieFlaggedAsFavourite(movieId).fold(
                    ifLeft = { error: Error -> error },
                    ifRight = { isMovieFavourite ->
                        moviesLocalDataSource.saveMovieDetails(movie.copy(isFavourite = isMovieFavourite))
                    }
                )
            }
        )
    }

    suspend fun switchMovieFavouriteFlag(movieId: Long, toFavourite: Boolean): Error? =
        moviesLocalDataSource.switchMovieFavouriteFlag(movieId, toFavourite)

    fun buildMovieImageUrl(
        movieImageRelativePathUrl: String,
        imageSize: MovieImageSize
    ): String =
        moviesRemoteDataSource.buildUrlMovieImage(movieImageRelativePathUrl, imageSize)
}
