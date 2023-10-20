package com.example.mymovies.data.database

import com.example.mymovies.domain.ItemsPaginationInfo
import com.example.mymovies.domain.Movie
import com.example.mymovies.domain.MovieGenre
import com.example.mymovies.data.database.Movie as MovieDatabaseModel
import com.example.mymovies.data.database.MovieGenre as MovieGenreDatabaseModel

fun MovieDatabaseModel.toDomainModel(): Movie =
    Movie(
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
        localTitle,
        voteAverage,
        voteCount,
        emptyList(),
        isFavourite
    )

fun Movie.toDatabaseModel(): MovieDatabaseModel =
    MovieDatabaseModel(
        id,
        backdropImageRelativePath,
        posterImageRelativePath,
        budget,
        originalLanguage,
        originalTitle,
        overview,
        popularity,
        releaseDate,
        revenue,
        runningTime,
        localTitle,
        voteAverage,
        voteCount,
        isFavourite
    )

fun MovieGenreDatabaseModel.toDomainModel(): MovieGenre =
    MovieGenre(id, name)

fun MovieGenre.toDatabaseModel(): MovieGenreDatabaseModel =
    MovieGenreDatabaseModel(id, name)

fun MoviesPaginationInfo.toDomainModel(): ItemsPaginationInfo =
    ItemsPaginationInfo(
        lastPageLoaded,
        totalPages,
        totalMovies
    )

fun ItemsPaginationInfo.toMoviesPaginationInfoDatabaseModel(): MoviesPaginationInfo =
    MoviesPaginationInfo(
        lastPageLoaded = lastPageLoaded,
        totalPages = totalPages,
        totalMovies = totalItems
    )
