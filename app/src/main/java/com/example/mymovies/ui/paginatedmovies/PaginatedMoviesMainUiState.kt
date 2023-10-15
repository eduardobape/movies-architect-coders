package com.example.mymovies.ui.paginatedmovies

import com.example.mymovies.domain.Error
import com.example.mymovies.domain.ItemsPaginationInfo
import com.example.mymovies.domain.MoviesSearchFilters

data class PaginatedMoviesMainUiState(
    val isLoading: Boolean = false,
    val movies: List<PaginatedMovieUiModel> = emptyList(),
    val paginationInfo: ItemsPaginationInfo = ItemsPaginationInfo(),
    val error: Error? = null,
    val moviesSearchFilters: MoviesSearchFilters = MoviesSearchFilters(),
    val isNeededRetryCollectMovies: Boolean = false,
    val isNeededRetryFetchMovies: Boolean = false
)

data class PaginatedMovieUiModel(
    val id: Long,
    val posterUrl: String?,
    val localTitle: String,
    val isFavourite: Boolean
)

fun PaginatedMovieUiModel.hasPoster(): Boolean = posterUrl != null
