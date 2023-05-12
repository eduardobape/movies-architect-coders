package com.example.mymovies.data.remoteapi.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MovieDetailsResult(
    @Json(name = "id") val id: Int,
    @Json(name = "title") val translatedTitle: String,
    @Json(name = "original_title") val originalTitle: String,
    @Json(name = "poster_path") val posterPath: String?,
    @Json(name = "overview") val overview: String?,
    @Json(name = "release_date") val releaseDate: String,
    @Json(name = "genres") val genres: List<MovieGenre>,
    @Json(name = "vote_average") val voteAverage: Float
)

@JsonClass(generateAdapter = true)
data class MovieGenre(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String
)
