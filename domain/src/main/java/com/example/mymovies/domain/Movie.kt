package com.example.mymovies.domain

data class Movie(
    val id: Long,
    val backdropImageRelativePath: String?,
    val posterImageRelativePath: String?,
    val budget: Long,
    val originalLanguage: String,
    val originalTitle: String,
    val overview: String?,
    val popularity: Float,
    val releaseDate: String,
    val revenue: Long,
    val runningTime: Int,
    val localTitle: String,
    val voteAverage: Float,
    val voteCount: Int,
    val genres: List<MovieGenre>,
    val isFavourite: Boolean
)

data class MovieGenre(
    val id: Int,
    val name: String
)

enum class MovieImageSize(val width: String) {
    WIDTH_92PX("w92"),
    WIDTH_154PX("w154"),
    WIDTH_185PX("w185"),
    WIDTH_342PX("w342"),
    WIDTH_500PX("w500"),
    WIDTH_780PX("w780"),
    WIDTH_ORIGINAL("original"),
}

data class PaginatedMovies(
    val movies: List<Movie> = emptyList(),
    val paginationInfo: ItemsPaginationInfo = ItemsPaginationInfo()
)

data class ItemsPaginationInfo(
    val lastPageLoaded: Int = 0,
    val totalPages: Int = 0,
    val totalItems: Int = 0
)
