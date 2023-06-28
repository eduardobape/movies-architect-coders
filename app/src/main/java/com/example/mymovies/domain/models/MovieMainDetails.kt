package com.example.mymovies.domain.models

import com.example.mymovies.data.remote.models.MoviesDiscoveryResult

data class MoviesDiscoveryDetails(
	val pages: Int = 0,
	val page: Int = 0,
	val movies: List<MovieMainDetails> = emptyList()
)

data class MovieMainDetails(
	val id: Int,
	val originalTitle: String,
	val translatedTitle: String,
	val posterPath: String?
) {
	fun hasPoster(): Boolean = posterPath != null
}

fun MoviesDiscoveryResult.toDomainModel(): MoviesDiscoveryDetails = MoviesDiscoveryDetails(
	totalPages,
	page,
	movies.map { movie ->
		MovieMainDetails(
			movie.id,
			movie.originalTitle,
			movie.translatedTitle,
			movie.posterPath
		)
	}
)
