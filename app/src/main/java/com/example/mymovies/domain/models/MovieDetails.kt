package com.example.mymovies.domain.models

data class MovieDetails(
    val id: Long,
    val translatedTitle: String,
    val originalTitle: String,
    val overview: String?,
    val releaseDate: String,
    val genres: List<String>,
    val voteAverage: Float,
    val posterPath: String?,
    val backdropImagePath: String?
)
