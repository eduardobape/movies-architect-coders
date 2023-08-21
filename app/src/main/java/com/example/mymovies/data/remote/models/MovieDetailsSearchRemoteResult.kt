package com.example.mymovies.data.remote.models

import com.example.mymovies.domain.models.MovieDetails
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MovieDetailsSearchRemoteResult(
    @Json(name = "adult") val isAdultRating: Boolean,
    @Json(name = "backdrop_path") val backdropUrlPath: String?,
    @Json(name = "belongs_to_collection") val moviesCollection: MoviesCollection?,
    @Json(name = "budget") val budget: Long,
    @Json(name = "genres") val genres: List<RemoteMovieGenre>,
    @Json(name = "homepage") val homepageUrl: String?,
    @Json(name = "id") val id: Int,
    @Json(name = "imdb_id") val imdbId: String?,
    @Json(name = "original_language") val originalLanguage: String,
    @Json(name = "original_title") val originalTitle: String,
    @Json(name = "overview") val overview: String?,
    @Json(name = "popularity") val popularity: Float,
    @Json(name = "poster_path") val posterUrlPath: String?,
    @Json(name = "production_companies") val productionCompanies: List<ProductionCompany>,
    @Json(name = "production_countries") val productionCountries: List<ProductionCountry>,
    @Json(name = "release_date") val releaseDate: String,
    @Json(name = "revenue") val revenue: Long,
    @Json(name = "runtime") val runningTime: Int,
    @Json(name = "spoken_languages") val spokenLanguages: List<MovieLanguage>,
    @Json(name = "status") val status: String?,
    @Json(name = "tagline") val tagline: String?,
    @Json(name = "title") val translatedTitle: String,
    @Json(name = "video") val isVideoMovie: Boolean,
    @Json(name = "vote_average") val voteAverage: Float,
    @Json(name = "vote_count") val voteCount: Int
)

@JsonClass(generateAdapter = true)
data class MoviesCollection(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String,
    @Json(name = "poster_path") val posterPathUrl: String?,
    @Json(name = "backdrop_path") val backdropImagePathUrl: String?
)

@JsonClass(generateAdapter = true)
data class RemoteMovieGenre(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String
)

@JsonClass(generateAdapter = true)
data class ProductionCompany(
    @Json(name = "id") val id: Int,
    @Json(name = "logo_path") val logoPathUrl: String?,
    @Json(name = "name") val name: String,
    @Json(name = "origin_country") val originCountry: String
)

@JsonClass(generateAdapter = true)
data class ProductionCountry(
    @Json(name = "iso_3166_1") val isoCodeCountry: String,
    @Json(name = "name") val countryName: String
)

@JsonClass(generateAdapter = true)
data class MovieLanguage(
    @Json(name = "english_name") val nameInEnglish: String,
    @Json(name = "iso_639_1") val isoCodeLanguage: String,
    @Json(name = "name") val name: String

)

fun MovieDetailsSearchRemoteResult.toDomainModel(): MovieDetails = MovieDetails(
    id,
    translatedTitle,
    originalTitle,
    overview,
    releaseDate,
    genres.map { it.name },
    voteAverage,
    posterUrlPath,
    backdropUrlPath
)