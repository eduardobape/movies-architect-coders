package com.example.mymovies.domain.usecases

import com.example.mymovies.data.remote.services.ApiUrlsManager.ApiImageUtils

object GetUrlMoviePosterUseCase {
    operator fun invoke(moviePosterPath: String, imageSize: ApiImageUtils.PosterMovieSize): String =
        ApiImageUtils.buildFullUrlImage(moviePosterPath, imageSize)
}