package com.example.mymovies.domain.usecases

import com.example.mymovies.data.remoteapi.ApiUrlsManager.ApiImageUtils

object GetUrlMovieBackdropUseCase {
    operator fun invoke(
        backdropMoviePath: String,
        backdropImageSize: ApiImageUtils.PosterMovieSize
    ): String = ApiImageUtils.buildFullUrlImage(backdropMoviePath, backdropImageSize)
}