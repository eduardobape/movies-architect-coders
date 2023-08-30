package com.example.mymovies.data.local.database.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "movies")
data class Movie(
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "backdrop_path_url") val backdropPathUrl: String?,
    @ColumnInfo(name = "poster_path_url") val posterPathUrl: String?,
    val budget: Long,
    @ColumnInfo(name = "original_language") val originalLanguage: String,
    @ColumnInfo(name = "original_title") val originalTitle: String,
    val overview: String?,
    val popularity: Float,
    @ColumnInfo(name = "release_date") val releaseDate: String,
    val revenue: Long,
    @ColumnInfo(name = "running_time") val runningTime: Int,
    val title: String,
    @ColumnInfo(name = "vote_average") val voteAverage: Float,
    @ColumnInfo(name = "vote_count") val voteCount: Int
)

@Entity(tableName = "movie_genres")
data class MovieGenre(
    @PrimaryKey val id: Int,
    val name: String
)

/**
 * Associative entity between the LocalMovie and LocalMovieGenre entities
 */
@Entity(
    tableName = "movies_genres_cross_ref",
    primaryKeys = ["movie_id", "genre_id"],
    foreignKeys = [ForeignKey(
        entity = Movie::class,
        parentColumns = ["id"],
        childColumns = ["movie_id"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    ), ForeignKey(
        entity = MovieGenre::class,
        parentColumns = ["id"],
        childColumns = ["genre_id"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )]
)
data class MoviesGenresCrossRef(
    @ColumnInfo(name = "movie_id") val movieId: Long,
    @ColumnInfo(name = "genre_id", index = true) val genreId: Int
)

/**
 * This class manage the Many to Many relationship between the LocalMovie and LocalMovieGenre entities
 */
data class MovieWithGenres(
    @Embedded val movie: Movie,
    @Relation(
        entity = MovieGenre::class,
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = MoviesGenresCrossRef::class,
            parentColumn = "movie_id",
            entityColumn = "genre_id"
        )
    )
    val genres: List<MovieGenre>
)
