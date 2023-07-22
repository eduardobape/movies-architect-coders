package com.example.mymovies.domain.models

import com.example.mymovies.data.remote.models.MovieDetailsDiscovery
import com.example.mymovies.data.remote.models.MoviesDiscoveryRemote

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

fun MoviesDiscoveryRemote.toDomainModel(): MoviesDiscoveryDetails = MoviesDiscoveryDetails(
	totalPages,
	page,
	movies.map(MovieDetailsDiscovery::toDomainModel)
)

fun MovieDetailsDiscovery.toDomainModel(): MovieMainDetails = MovieMainDetails(
	id = id,
	originalTitle = originalTitle,
	translatedTitle = translatedTitle,
	posterPath = posterPath
)
