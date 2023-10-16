package com.example.mymovies.data

import com.example.mymovies.data.datasources.MoviesLocalDataSource
import com.example.mymovies.data.datasources.MoviesRemoteDataSource
import com.example.mymovies.domain.Error
import com.example.mymovies.domain.ItemsPaginationInfo
import com.example.mymovies.domain.Movie
import com.example.mymovies.domain.MovieImageSize
import com.example.mymovies.domain.MoviesSearchFilters
import com.example.mymovies.domain.PaginatedMovies
import com.example.mymovies.framework.shared.toError
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
        return runCatching {
            val paginatedMovies: PaginatedMovies =
                moviesRemoteDataSource.requestPaginatedMovies(moviesSearchFilters, pageToFetch)
            moviesLocalDataSource.savePaginatedMovies(paginatedMovies)
        }.fold({ null }) { exception -> exception.toError() }
    }

    fun getMovieDetailsWithGenres(movieId: Long): Flow<Movie> =
        moviesLocalDataSource.getMovieWithGenres(movieId)

    suspend fun requestMovieDetails(movieId: Long): Error? {
        return runCatching {
            val movie: Movie = moviesRemoteDataSource.requestMovieDetails(movieId)
            val isMovieFavourite: Boolean = moviesLocalDataSource.isMovieFlaggedAsFavourite(movieId)
            moviesLocalDataSource.saveMovieDetails(movie.copy(isFavourite = isMovieFavourite))
        }.fold(
            { null }
        ) { exception -> exception.toError() }
    }

    suspend fun switchMovieFavouriteFlag(movieId: Long, toFavourite: Boolean): Error? = runCatching {
        moviesLocalDataSource.switchMovieFavouriteFlag(movieId, toFavourite)
    }.fold({ null }) { exception -> exception.toError() }

    fun buildMovieImageUrl(
        movieImageRelativePathUrl: String,
        imageSize: MovieImageSize
    ): String =
        moviesRemoteDataSource.buildUrlMovieImage(movieImageRelativePathUrl, imageSize)
}
