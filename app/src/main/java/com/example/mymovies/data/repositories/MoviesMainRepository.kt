package com.example.mymovies.data.repositories

import com.example.mymovies.App
import com.example.mymovies.data.datasources.MoviesLocalDataSource
import com.example.mymovies.data.datasources.MoviesRemoteDataSource
import com.example.mymovies.data.datasources.PaginationInfoLocalDataSource
import com.example.mymovies.data.local.datastore.dataStore
import com.example.mymovies.data.local.models.PaginatedMoviesDatabase
import com.example.mymovies.data.local.models.PaginationInfo
import com.example.mymovies.data.remote.models.PaginatedMoviesSearchRemoteResult
import com.example.mymovies.data.remote.models.toDatabaseModel
import com.example.mymovies.data.remote.services.MoviesApiServices
import com.example.mymovies.data.remote.services.RetrofitApiServices
import com.example.mymovies.ui.models.MoviesSearchFilters
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

class MoviesMainRepository(application: App) {

    companion object {
        const val PAGE_SIZE = 20
    }

    private val mainMoviesRemoteDataSource = MoviesRemoteDataSource(
        MoviesApiServices(RetrofitApiServices).moviesSearchApiService
    )
    private val moviesLocalDataSource = MoviesLocalDataSource(application.database.movieDao())
    private val moviesPaginationInfoLocalDataSource = PaginationInfoLocalDataSource(application.dataStore)

    @OptIn(ExperimentalCoroutinesApi::class)
    val popularPaginatedMovies: Flow<PaginatedMoviesDatabase> =
        moviesPaginationInfoLocalDataSource.popularMoviesPaginationInfo
            .flatMapLatest { paginationInfo ->
                moviesLocalDataSource.popularMovies
                    .filter { popularMovies -> popularMovies.size == paginationInfo.currentPage * PAGE_SIZE }
                    .map { popularMovies ->
                        PaginatedMoviesDatabase(
                            popularMovies,
                            PaginationInfo(
                                paginationInfo.currentPage,
                                paginationInfo.totalPages,
                                paginationInfo.totalItems
                            )
                        )
                    }
            }


    suspend fun findPaginatedPopularMovies(
        moviesSearchFilters: MoviesSearchFilters,
        pageToFetch: Int
    ) {
        val popularPaginatedMoviesSearchRemoteResult: PaginatedMoviesSearchRemoteResult =
            mainMoviesRemoteDataSource.findPopularMovies(moviesSearchFilters, pageToFetch)
        moviesPaginationInfoLocalDataSource.updatePopularMoviesPaginationInfo(
            PaginationInfo(
                popularPaginatedMoviesSearchRemoteResult.page,
                popularPaginatedMoviesSearchRemoteResult.totalPages,
                popularPaginatedMoviesSearchRemoteResult.totalResults,
            )
        )
        moviesLocalDataSource.saveMovies(popularPaginatedMoviesSearchRemoteResult.movies.map { it.toDatabaseModel() })
    }
}
