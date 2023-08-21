package com.example.mymovies.data.repositories

import com.example.mymovies.App
import com.example.mymovies.data.datasources.MoviesLocalDataSource
import com.example.mymovies.data.local.models.PaginationInfo
import com.example.mymovies.data.datasources.PaginationInfoLocalDataSource
import com.example.mymovies.data.datasources.MoviesRemoteDataSource
import com.example.mymovies.data.remote.services.RetrofitApiServices
import com.example.mymovies.data.remote.models.MoviesSearchRemoteResult
import com.example.mymovies.data.remote.models.toDatabaseModel
import com.example.mymovies.data.remote.services.MoviesApiServices
import com.example.mymovies.data.local.datastore.dataStore
import com.example.mymovies.data.local.models.PaginatedMoviesDatabase
import com.example.mymovies.ui.models.MoviesSearchFilters
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.zip

class MoviesMainRepository(application: App) {

    private val mainMoviesRemoteDataSource = MoviesRemoteDataSource(
        MoviesApiServices(RetrofitApiServices).moviesSearchApiService
    )
    private val moviesLocalDataSource = MoviesLocalDataSource(application.database.movieDao())
    private val moviesPaginationInfoLocalDataSource = PaginationInfoLocalDataSource(application.dataStore)

    val popularPaginatedMovies: Flow<PaginatedMoviesDatabase> = moviesLocalDataSource.popularMovies
        .zip(moviesPaginationInfoLocalDataSource.popularMoviesPaginationInfo) { popularMovies, paginationInfo ->
            PaginatedMoviesDatabase(
                popularMovies,
                PaginationInfo(
                    paginationInfo.currentPage,
                    paginationInfo.totalPages,
                    paginationInfo.totalItems
                )
            )
        }

    suspend fun findPaginatedPopularMovies(
        moviesSearchFilters: MoviesSearchFilters,
        pageToFetch: Int
    ) {
        val popularMoviesSearchRemoteResult: MoviesSearchRemoteResult =
            mainMoviesRemoteDataSource.findPopularMovies(moviesSearchFilters, pageToFetch)
        moviesPaginationInfoLocalDataSource.updatePopularMoviesPaginationInfo(
            PaginationInfo(
                popularMoviesSearchRemoteResult.page,
                popularMoviesSearchRemoteResult.totalPages,
                popularMoviesSearchRemoteResult.totalResults,
            )
        )
        moviesLocalDataSource.saveMovies(popularMoviesSearchRemoteResult.movies.map { it.toDatabaseModel() })
    }
}
