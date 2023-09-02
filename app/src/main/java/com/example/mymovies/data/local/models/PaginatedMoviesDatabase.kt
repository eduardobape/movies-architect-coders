package com.example.mymovies.data.local.models

import com.example.mymovies.data.local.database.entities.Movie
import com.example.mymovies.domain.models.PaginatedMovieDetails
import com.example.mymovies.domain.models.PaginatedMovies

data class PaginatedMoviesDatabase(
    val movies: List<Movie> = emptyList(),
    val paginationInfo: PaginationInfo
)

data class PaginationInfo(
    val currentPage: Int = 0,
    val totalPages: Int = 0,
    val totalItems: Int = 0
)

fun PaginatedMoviesDatabase.toDomain(): PaginatedMovies = PaginatedMovies(
    paginationInfo.totalPages,
    paginationInfo.currentPage,
    movies.map { localMovie ->
        PaginatedMovieDetails(
            localMovie.id,
            localMovie.originalTitle,
            localMovie.title,
            localMovie.posterPathUrl,
            localMovie.isFavourite
        )
    }
)
