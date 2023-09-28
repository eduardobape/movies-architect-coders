package com.example.mymovies.data.local.database.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.mymovies.data.local.database.entities.Movie
import com.example.mymovies.data.local.database.entities.MovieGenre
import com.example.mymovies.data.local.database.entities.MovieWithGenres
import com.example.mymovies.data.local.database.entities.MoviesGenresCrossRef
import com.example.mymovies.data.local.database.entities.MoviesPaginationInfo
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Upsert
    suspend fun saveMovie(movie: Movie)

    @Upsert
    suspend fun saveMovies(movies: List<Movie>)

    @Transaction
    suspend fun savePaginatedMovies(movies: List<Movie>, moviesPaginationInfo: MoviesPaginationInfo) {
        updateMoviesPaginationInfo(moviesPaginationInfo)
        saveMovies(movies)
    }

    @Upsert
    suspend fun updateMoviesPaginationInfo(moviesPaginationInfo: MoviesPaginationInfo)

    @Query("SELECT * FROM movies_pagination_info")
    suspend fun getMoviesPaginationInfo(): MoviesPaginationInfo?

    @Upsert
    suspend fun saveMovieGenres(movieGenres: List<MovieGenre>)

    @Upsert
    suspend fun saveMovieAndGenresRelation(movieWithGenres: List<MoviesGenresCrossRef>)

    @Transaction
    suspend fun saveMovieGenresAndRelation(movieDetailsWithGenres: MovieWithGenres) {
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

    @Query("UPDATE movies SET favourite = :toFavourite WHERE id = :movieId")
    suspend fun switchMovieFavourite(movieId: Long, toFavourite: Boolean)
}
