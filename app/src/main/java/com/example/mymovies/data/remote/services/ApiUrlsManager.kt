package com.example.mymovies.data.remote.services

class ApiUrlsManager {
	companion object {
		const val theMovieDbBaseUrl = "https://api.themoviedb.org/3/"
		const val imageBaseUrl = "https://image.tmdb.org/t/p/"
	}

	object ApiImageUtils {

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
}
