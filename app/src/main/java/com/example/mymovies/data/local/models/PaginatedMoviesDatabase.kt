package com.example.mymovies.data.local.models

import com.example.mymovies.data.local.database.entities.Movie
import com.example.mymovies.data.local.database.entities.MoviesPaginationInfo
import com.example.mymovies.domain.models.PaginatedMovieDetails
import com.example.mymovies.domain.models.PaginatedMovies

data class PaginatedMoviesDatabase(
    val movies: List<Movie> = emptyList(),
    val moviesPaginationInfo: MoviesPaginationInfo
)

fun PaginatedMoviesDatabase.toDomain(): PaginatedMovies = PaginatedMovies(
    moviesPaginationInfo.totalPages,
    moviesPaginationInfo.lastPageLoaded,
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
