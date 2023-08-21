package com.example.mymovies.domain.usecases

import com.example.mymovies.data.remote.apiurls.ApiUrlsManager

object BuildUrlMoviePosterUseCase {
    operator fun invoke(moviePosterPathUrl: String, imageSize: ApiUrlsManager.PosterMovieSize): String =
        ApiUrlsManager.buildFullUrlImage(moviePosterPathUrl, imageSize)
}
