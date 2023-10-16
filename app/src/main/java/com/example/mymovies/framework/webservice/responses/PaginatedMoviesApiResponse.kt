package com.example.mymovies.framework.webservice.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PaginatedMoviesApiResponse(
    @Json(name = "results") val movies: List<PaginatedMovieApiResponse> = emptyList(),
    @Json(name = "page") val pageRequested: Int = 0,
    @Json(name = "total_pages") val totalPages: Int = 0,
    @Json(name = "total_results") val totalMovies: Int = 0,
)

@JsonClass(generateAdapter = true)
data class PaginatedMovieApiResponse(
    @Json(name = "adult") val isAdultRating: Boolean,
    @Json(name = "backdrop_path") val backdropPathUrl: String?,
    @Json(name = "genre_ids") val genreIds: List<Int>,
    @Json(name = "id") val id: Long,
    @Json(name = "original_language") val originalLanguage: String,
    @Json(name = "original_title") val originalTitle: String,
    @Json(name = "overview") val overview: String?,
    @Json(name = "popularity") val popularity: Float,
    @Json(name = "poster_path") val posterPathUrl: String?,
    @Json(name = "release_date") val releaseDate: String,
    @Json(name = "title") val localTitle: String,
    @Json(name = "video") val isRegularMovie: Boolean,
    @Json(name = "vote_average") val voteAverage: Float,
    @Json(name = "vote_count") val voteCount: Int
)
