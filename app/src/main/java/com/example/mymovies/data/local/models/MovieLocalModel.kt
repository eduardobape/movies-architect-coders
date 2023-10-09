package com.example.mymovies.data.local.models

import com.example.mymovies.data.local.database.entities.Movie as MovieDatabase
import com.example.mymovies.data.local.database.entities.MovieGenre as MovieGenreDatabase

data class Movie(
    val id: Long,
    val backdropPathUrl: String?,
    val posterPathUrl: String?,
    val budget: Long,
    val originalLanguage: String,
    val originalTitle: String,
    val overview: String?,
    val popularity: Float,
    val releaseDate: String,
    val revenue: Long,
    val runningTime: Int,
    val title: String,
    val voteAverage: Float,
    val voteCount: Int,
    val genres: List<MovieGenre>,
    val isFavourite: Boolean
)

data class MovieGenre(
    val id: Int,
    val name: String
)

fun Movie.toMovieModelDatabase(): MovieDatabase = MovieDatabase(
    id,
    backdropPathUrl,
    posterPathUrl,
    budget,
    originalLanguage,
    originalTitle,
    overview,
    popularity,
    releaseDate,
    revenue,
    runningTime,
    title,
    voteAverage,
    voteCount,
    isFavourite
)

fun MovieGenre.toMovieGenreModelDatabase(): MovieGenreDatabase = MovieGenreDatabase(id, name)

