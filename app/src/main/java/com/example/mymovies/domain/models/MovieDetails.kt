package com.example.mymovies.domain.models

import com.example.mymovies.data.remote.models.MovieDetailsResult

data class MovieDetails(
    val id: Int,
    val translatedTitle: String,
    val originalTitle: String,
    val overview: String?,
    val releaseDate: String,
    val genres: List<String>,
    val voteAverage: Float,
    val posterPath: String?,
    val backdropImagePath: String?
)

fun MovieDetailsResult.toDomainModel(): MovieDetails = MovieDetails(
    id,
    translatedTitle,
    originalTitle,
    overview,
    releaseDate,
    genres.map { it.name },
    voteAverage,
    posterPath,
    backdropImagePath
)
