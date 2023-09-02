package com.example.mymovies.data.remote.models

import com.example.mymovies.data.local.database.entities.Movie
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PaginatedMoviesSearchRemoteResult(
	@Json(name = "page") val page: Int = 0,
	@Json(name = "results") val movies: List<PaginatedMovieDetailsRemoteResult> = emptyList(),
	@Json(name = "total_pages") val totalPages: Int = 0,
	@Json(name = "total_results") val totalResults: Int = 0,
)

@JsonClass(generateAdapter = true)
data class PaginatedMovieDetailsRemoteResult(
	@Json(name = "adult") val isAdultRating: Boolean,
	@Json(name = "backdrop_path") val backdropUrlPath: String?,
	@Json(name = "genre_ids") val genreIds: List<Int>,
	@Json(name = "id") val id: Long,
	@Json(name = "original_language") val originalLanguage: String,
	@Json(name = "original_title") val originalTitle: String,
	@Json(name = "overview") val overview: String?,
	@Json(name = "popularity") val popularity: Float,
	@Json(name = "poster_path") val posterUrlPath: String?,
	@Json(name = "release_date") val releaseDate: String,
	@Json(name = "title") val translatedTitle: String,
	@Json(name = "video") val isRegularMovie: Boolean,
	@Json(name = "vote_average") val voteAverage: Float,
	@Json(name = "vote_count") val voteCount: Int
)

fun PaginatedMovieDetailsRemoteResult.toDatabaseModel(): Movie = Movie(
	id,
	backdropUrlPath,
	posterUrlPath,
	budget = 0,
	originalLanguage,
	originalTitle,
	overview,
	popularity,
	releaseDate,
	revenue = 0,
	runningTime = 0,
	translatedTitle,
	voteAverage,
	voteCount,
	isFavourite = false
)
