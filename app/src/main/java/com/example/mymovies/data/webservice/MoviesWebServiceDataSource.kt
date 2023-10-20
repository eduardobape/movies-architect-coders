package com.example.mymovies.data.webservice

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.example.mymovies.data.datasources.MoviesRemoteDataSource
import com.example.mymovies.data.toError
import com.example.mymovies.domain.Error
import com.example.mymovies.domain.Movie
import com.example.mymovies.domain.MovieImageSize
import com.example.mymovies.domain.MoviesSearchFilters
import com.example.mymovies.domain.PaginatedMovies
import java.util.Locale

class MoviesWebServiceDataSource(private val moviesSearchApiService: MoviesSearchApiService) : MoviesRemoteDataSource {

    companion object {
        const val apiBaseUrl = "https://api.themoviedb.org/3/"
        private const val imageBaseUrl = "https://image.tmdb.org/t/p/"
    }

    override suspend fun requestPaginatedMovies(
        moviesSearchFilters: MoviesSearchFilters, pageToFetch: Int
    ): Either<Error, PaginatedMovies> =
        try {
            moviesSearchApiService.requestPaginatedMovies(
                moviesSearchFilters.region,
                moviesSearchFilters.language,
                pageToFetch
            ).toDomainModel().right()
        } catch (e: Exception) {
            e.toError().left()
        }

    override suspend fun requestMovieDetails(movieId: Long): Either<Error, Movie> =
        try {
            moviesSearchApiService.fetchMovieDetails(movieId, Locale.getDefault().language).toDomainModel().right()
        } catch (e: Exception) {
            e.toError().left()
        }

    override fun buildUrlMovieImage(movieImageRelativePathUrl: String, imageSize: MovieImageSize): String {
        return "$imageBaseUrl${imageSize.width}$movieImageRelativePathUrl"
    }
}
