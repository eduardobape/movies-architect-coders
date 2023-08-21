package com.example.mymovies.data.remote.apiurls

object ApiUrlsManager {
	const val apiBaseUrl = "https://api.themoviedb.org/3/"
	private const val imageBaseUrl = "https://image.tmdb.org/t/p/"

	enum class PosterMovieSize(val width: String) {
		WIDTH_92PX("w92"),
		WIDTH_154PX("w154"),
		WIDTH_185PX("w185"),
		WIDTH_342PX("w342"),
		WIDTH_500PX("w500"),
		WIDTH_780PX("w780"),
		WIDTH_ORIGINAL("original"),
	}

	fun buildFullUrlImage(imagePath: String, size: PosterMovieSize): String {
		return "$imageBaseUrl${size.width}$imagePath"
	}
}
