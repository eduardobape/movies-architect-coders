package com.example.mymovies.framework.webservice

import com.example.mymovies.data.datasources.MoviesRemoteDataSource
import com.example.mymovies.domain.Movie
import com.example.mymovies.domain.MovieImageSize
import com.example.mymovies.domain.MoviesSearchFilters
import com.example.mymovies.domain.PaginatedMovies
import com.example.mymovies.framework.webservice.responses.toDomainModel
import com.example.mymovies.framework.webservice.responses.toMovieDomainModel
import com.example.mymovies.framework.webservice.services.MoviesSearchApiService
import java.util.Locale

class MoviesWebServiceDataSource(private val moviesSearchApiService: MoviesSearchApiService) : MoviesRemoteDataSource {

    companion object {
        const val apiBaseUrl = "https://api.themoviedb.org/3/"
        private const val imageBaseUrl = "https://image.tmdb.org/t/p/"
    }

    override suspend fun requestPaginatedMovies(
        moviesSearchFilters: MoviesSearchFilters, pageToFetch: Int
    ): PaginatedMovies =
        moviesSearchApiService.requestPaginatedMovies(
            moviesSearchFilters.region,
            moviesSearchFilters.language,
            pageToFetch
        ).toDomainModel()

    override suspend fun requestMovieDetails(movieId: Long): Movie =
        moviesSearchApiService.fetchMovieDetails(movieId, Locale.getDefault().language).toMovieDomainModel()

    override fun buildUrlMovieImage(movieImageRelativePathUrl: String, imageSize: MovieImageSize): String {
        return "$imageBaseUrl${imageSize.width}$movieImageRelativePathUrl"
    }
}
