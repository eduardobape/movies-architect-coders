package com.example.mymovies.framework.database.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.mymovies.framework.database.entities.Movie
import com.example.mymovies.framework.database.entities.MovieGenre
import com.example.mymovies.framework.database.entities.MovieWithGenres
import com.example.mymovies.framework.database.entities.MoviesGenresCrossRef
import com.example.mymovies.framework.database.entities.MoviesPaginationInfo
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Transaction
    suspend fun savePaginatedMovies(movies: List<Movie>, moviesPaginationInfo: MoviesPaginationInfo) {
        updateMoviesPaginationInfo(moviesPaginationInfo)
        saveMovies(movies)
    }

    @Upsert
    suspend fun saveMovies(movies: List<Movie>)

    @Upsert
    suspend fun saveMovie(movie: Movie)

    @Upsert
    suspend fun updateMoviesPaginationInfo(moviesPaginationInfo: MoviesPaginationInfo)

    @Query("SELECT * FROM movies_pagination_info")
    suspend fun getMoviesPaginationInfo(): MoviesPaginationInfo?

    @Transaction
    suspend fun saveMovieGenresAndRelationship(movieId: Long, movieGenres: List<MovieGenre>) {
        saveMovieGenres(movieGenres)
        saveMoviesAndGenresRelation(
            movieGenres.map {
                MoviesGenresCrossRef(movieId, it.id)
            }
        )
    }

    @Upsert
    suspend fun saveMovieGenres(movieGenres: List<MovieGenre>)

    @Upsert
    suspend fun saveMoviesAndGenresRelation(movieWithGenres: List<MoviesGenresCrossRef>)

    @Query("SELECT * FROM movies ORDER BY popularity DESC")
    fun getAllMovies(): Flow<List<Movie>>

    @Transaction
    @Query("SELECT * FROM movies WHERE id = :movieId")
    fun findMovie(movieId: Long): Flow<MovieWithGenres>

    @Query("UPDATE movies SET favourite = :toFavourite WHERE id = :movieId")
    suspend fun switchFavouriteFlagOfMovie(movieId: Long, toFavourite: Boolean)

    @Query("SELECT favourite FROM movies WHERE id = :movieId")
    suspend fun isMovieFlaggedAsFavourite(movieId: Long): Boolean
}
