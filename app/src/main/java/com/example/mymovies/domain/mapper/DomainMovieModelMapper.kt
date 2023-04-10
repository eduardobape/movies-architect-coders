package com.example.mymovies.domain.mapper

import com.example.mymovies.data.apiservices.MovieDetailsDiscovery
import com.example.mymovies.domain.model.MovieMainDetails

object DomainMovieModelMapper {

	fun movieMainDetailsToDomain(movieDetailsDataModel: MovieDetailsDiscovery): MovieMainDetails =
		MovieMainDetails(
			movieDetailsDataModel.originalTitle,
			movieDetailsDataModel.translatedTitle,
			movieDetailsDataModel.posterPath
		)
}