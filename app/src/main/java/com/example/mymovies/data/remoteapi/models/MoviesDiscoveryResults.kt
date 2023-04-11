package com.example.mymovies.data.remoteapi.models

import com.example.mymovies.data.remoteapi.ApiImageUtils
import com.example.mymovies.domain.models.MovieMainDetails
import com.squareup.moshi.Json


data class MoviesDiscoveryResult(
	@Json(name = "page") val page: Int,
	@Json(name = "total_pages") val totalPages: Int,
	@Json(name = "total_results") val totalResults: Int,
	@Json(name = "results") val movies: List<MovieDetailsDiscovery>
)

data class MovieDetailsDiscovery(
	@Json(name = "adult") val isAdultFilm: Boolean,
	@Json(name = "backdrop_path") val backdropPath: String?,
	@Json(name = "genre_ids") val genreIds: List<Int>,
	@Json(name = "id") val id: Int,
	@Json(name = "original_language") val originalLanguage: String,
	@Json(name = "original_title") val originalTitle: String,
	@Json(name = "overview") val overview: String,
	@Json(name = "popularity") val popularity: Double,
	@Json(name = "poster_path") val posterPath: String?,
	@Json(name = "release_date") val releaseDate: String,
	@Json(name = "title") val translatedTitle: String,
	@Json(name = "video") val isRegularMovie: Boolean,
	@Json(name = "vote_average") val voteAverage: Double,
	@Json(name = "vote_count") val voteCount: Int
)

fun MoviesDiscoveryResult.asDomainModel(): List<MovieMainDetails> = movies.map { movie ->
	MovieMainDetails(
		movie.originalTitle,
		movie.translatedTitle,
		movie.posterPath?.let {
			ApiImageUtils.buildFullUrlImage(movie.posterPath, ApiImageUtils.PosterMovieSize.WIDTH_500PX)
		}
	)
}