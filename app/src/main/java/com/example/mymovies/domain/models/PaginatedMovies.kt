package com.example.mymovies.domain.models

data class PaginatedMovies(
    val pages: Int = 0,
    val page: Int = 0,
    val movies: List<PaginatedMovieDetails> = emptyList()
)

data class PaginatedMovieDetails(
    val id: Long,
    val originalTitle: String,
    val translatedTitle: String,
    val posterPath: String?,
    val isFavourite: Boolean
)

fun PaginatedMovieDetails.hasPoster(): Boolean = posterPath != null
