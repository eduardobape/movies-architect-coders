package com.example.mymovies.data.repositories

import com.example.mymovies.App
import com.example.mymovies.data.datasources.MoviesLocalDataSource
import com.example.mymovies.data.datasources.MoviesRemoteDataSource
import com.example.mymovies.data.errors.Error
import com.example.mymovies.data.errors.toError
import com.example.mymovies.data.local.database.entities.MoviesPaginationInfo
import com.example.mymovies.data.local.models.PaginatedMoviesDatabase
import com.example.mymovies.data.remote.models.PaginatedMoviesSearchRemoteResult
import com.example.mymovies.data.remote.models.toDatabaseModel
import com.example.mymovies.data.remote.services.MoviesApiServices
import com.example.mymovies.data.remote.services.RetrofitApiServices
import com.example.mymovies.ui.models.MoviesSearchFilters
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PaginatedMoviesRepository(application: App) {

    companion object {
        const val PAGE_SIZE = 20
    }

    private val mainMoviesRemoteDataSource = MoviesRemoteDataSource(
        MoviesApiServices(RetrofitApiServices).moviesSearchApiService
    )
    private val moviesLocalDataSource = MoviesLocalDataSource(application.database.movieDao())

    val popularPaginatedMovies: Flow<PaginatedMoviesDatabase> = moviesLocalDataSource.popularMovies
        .map { movies ->
            val moviesPaginationInfo: MoviesPaginationInfo? = moviesLocalDataSource.getMoviesPaginationInfo()
            PaginatedMoviesDatabase(
                movies,
                MoviesPaginationInfo(
                    lastPageLoaded = moviesPaginationInfo?.lastPageLoaded ?: 0,
                    totalPages = moviesPaginationInfo?.totalPages ?: 0,
                    totalPaginatedMovies = moviesPaginationInfo?.totalPaginatedMovies ?: 0
                )
            )
        }

    suspend fun findPaginatedPopularMovies(
        moviesSearchFilters: MoviesSearchFilters,
        pageToFetch: Int
    ): Error? {
        return runCatching {
            val popularPaginatedMoviesSearchRemoteResult: PaginatedMoviesSearchRemoteResult =
                mainMoviesRemoteDataSource.findPopularMovies(moviesSearchFilters, pageToFetch)
            moviesLocalDataSource.savePaginatedMovies(
                popularPaginatedMoviesSearchRemoteResult.movies.map { it.toDatabaseModel() },
                MoviesPaginationInfo(
                    lastPageLoaded = popularPaginatedMoviesSearchRemoteResult.page,
                    totalPages = popularPaginatedMoviesSearchRemoteResult.totalPages,
                    totalPaginatedMovies = popularPaginatedMoviesSearchRemoteResult.totalResults,
                )
            )
        }.fold({ null }) { exception -> exception.toError() }
    }
}
