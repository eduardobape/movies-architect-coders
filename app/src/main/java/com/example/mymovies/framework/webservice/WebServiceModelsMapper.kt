package com.example.mymovies.framework.webservice

import com.example.mymovies.domain.ItemsPaginationInfo
import com.example.mymovies.domain.Movie
import com.example.mymovies.domain.MovieGenre
import com.example.mymovies.domain.PaginatedMovies
import com.example.mymovies.framework.webservice.responses.MovieDetailsSearchRemoteResult
import com.example.mymovies.framework.webservice.responses.MovieGenreRemote
import com.example.mymovies.framework.webservice.responses.PaginatedMovieApiResponse
import com.example.mymovies.framework.webservice.responses.PaginatedMoviesApiResponse

fun PaginatedMoviesApiResponse.toDomainModel(): PaginatedMovies =
    PaginatedMovies(
        movies.map { it.toDomainModel() },
        ItemsPaginationInfo(pageRequested, totalPages, totalMovies)
    )

fun PaginatedMovieApiResponse.toDomainModel(): Movie =
    Movie(
        id,
        backdropPathUrl,
        posterPathUrl,
        budget = 0,
        originalLanguage,
        originalTitle,
        overview,
        popularity,
        releaseDate,
        revenue = 0,
        runningTime = 0,
        localTitle,
        voteAverage,
        voteCount,
        genres = emptyList(),
        isFavourite = false
    )

fun MovieDetailsSearchRemoteResult.toDomainModel(): Movie =
    Movie(
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
        genres.map { it.toDomainModel() },
        isFavourite = false
    )

fun MovieGenreRemote.toDomainModel(): MovieGenre = MovieGenre(id, name)
