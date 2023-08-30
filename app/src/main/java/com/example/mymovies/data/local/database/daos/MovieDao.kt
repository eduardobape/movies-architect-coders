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

@Dao
interface MovieDao {

    @Upsert
    suspend fun saveMovies(movies: List<Movie>)

    @Upsert
    suspend fun saveMovie(movie: Movie)

    @Upsert
    suspend fun saveMovieGenres(movieGenre: List<MovieGenre>)

    @Upsert
    suspend fun saveMovieAndGenresRelation(movieWithGenres: List<MoviesGenresCrossRef>)

    @Transaction
    suspend fun saveMovieDetailsWithGenres(movieDetailsWithGenres: MovieWithGenres) {
        saveMovie(movieDetailsWithGenres.movie)
        saveMovieGenres(movieDetailsWithGenres.genres)
        saveMovieAndGenresRelation(
            movieDetailsWithGenres.genres.map {
                MoviesGenresCrossRef(movieDetailsWithGenres.movie.id, it.id)
            }
        )
    }

    @Query("SELECT * FROM movies ORDER BY popularity DESC")
    fun findPopularMovies(): Flow<List<Movie>>

    @Transaction
    @Query("SELECT * FROM movies WHERE id = :movieId")
    fun findMovieWithGenres(movieId: Long): Flow<MovieWithGenres>

    @Query("SELECT COUNT(id) FROM movies WHERE id = :movieId")
    suspend fun isMovieSaved(movieId: Long): Int
}
