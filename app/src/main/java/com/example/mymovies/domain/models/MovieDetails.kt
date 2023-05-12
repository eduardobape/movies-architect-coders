package com.example.mymovies.domain.models

import com.example.mymovies.data.remoteapi.ApiUrlsManager
import com.example.mymovies.data.remoteapi.models.MovieDetailsResult

data class MovieDetails(
    val id: Int,
    val translatedTitle: String,
    val originalTitle: String,
    val overview: String?,
    val releaseDate: String,
    val genres: List<String>,
    val voteAverage: Float,
    val posterUrl: String?
)

fun MovieDetailsResult.toDomainModel(): MovieDetails = MovieDetails(
    id,
    translatedTitle,
    originalTitle,
    overview,
    releaseDate,
    genres.map { it.name },
    voteAverage,
    posterPath?.let {
        ApiUrlsManager.ApiImageUtils.buildFullUrlImage(
            it,
            ApiUrlsManager.ApiImageUtils.PosterMovieSize.WIDTH_500PX
        )
    }
)
