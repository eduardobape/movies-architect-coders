package com.example.mymovies.domain.usecases

import com.example.mymovies.data.remote.apiurls.ApiUrlsManager

object BuildUrlMovieBackdropUseCase {

    operator fun invoke(backdropMoviePathUrl: String, backdropImageSize: ApiUrlsManager.PosterMovieSize): String =
        ApiUrlsManager.buildFullUrlImage(backdropMoviePathUrl, backdropImageSize)
}
