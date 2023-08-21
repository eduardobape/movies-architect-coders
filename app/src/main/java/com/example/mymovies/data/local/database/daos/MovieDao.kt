package com.example.mymovies.data.local.database.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.mymovies.data.local.database.entities.Movie
import com.example.mymovies.data.local.database.entities.MovieGenre
import com.example.mymovies.data.local.database.entities.MovieWithGenres
import com.example.mymovies.data.local.database.entities.MoviesGenresCrossRef
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

@Dao
interface MovieDao {

    @Upsert
    suspend fun saveMovie(movie: Movie)

    @Upsert
    suspend fun saveMovies(movies: List<Movie>)

    @Upsert
    suspend fun saveMovieGenres(movieGenre: List<MovieGenre>)

    @Upsert
    suspend fun saveMovieWithGenres(movieWithGenres: List<MoviesGenresCrossRef>)

    @Query("SELECT * FROM movies ORDER BY popularity DESC")
    fun findPopularMovies(): Flow<List<Movie>>

    @Transaction
    @Query("SELECT * FROM movies WHERE id = :movieId")
    fun findMovieWithGenres(movieId: Int): Flow<MovieWithGenres>

    @Query("SELECT * FROM movies WHERE id = :movieId")
    fun findMovieById(movieId: Int): Flow<Movie>

    fun findMovieByIdDistinct(movieId: Int): Flow<Movie> = findMovieById(movieId).distinctUntilChanged()
}
