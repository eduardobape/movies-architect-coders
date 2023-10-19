package com.example.mymovies.framework.database

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.example.mymovies.data.datasources.MoviesLocalDataSource
import com.example.mymovies.domain.Error
import com.example.mymovies.domain.ItemsPaginationInfo
import com.example.mymovies.domain.Movie
import com.example.mymovies.domain.PaginatedMovies
import com.example.mymovies.framework.toError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MoviesRoomDataSource(private val movieDao: MovieDao) : MoviesLocalDataSource {

    override val allCachedMovies: Flow<List<Movie>>
        get() = movieDao.getAllMovies().map { movies ->
            movies.map { it.toDomainModel() }
        }

    override suspend fun savePaginatedMovies(paginatedMovies: PaginatedMovies): Error? = try {
        movieDao.savePaginatedMovies(
            paginatedMovies.movies.map { it.toDatabaseModel() },
            paginatedMovies.paginationInfo.toMoviesPaginationInfoDatabaseModel()
        )
        null
    } catch (e: Exception) {
        e.toError()
    }

    override suspend fun getMoviesPaginationInfo(): ItemsPaginationInfo? {
        return movieDao.getMoviesPaginationInfo()?.toDomainModel()
    }

    override suspend fun saveMovieDetails(movie: Movie): Error? = try {
        movieDao.saveMovie(movie.toDatabaseModel())
        movieDao.saveMovieGenresAndRelationship(movie.id, movie.genres.map { it.toDatabaseModel() })
        null
    } catch (e: Exception) {
        e.toError()
    }

    override fun getMovieWithGenres(movieId: Long): Flow<Movie> =
        movieDao.findMovie(movieId).map { movieDb: MovieWithGenres ->
            val movie = movieDb.movie.toDomainModel()
            val genres = movieDb.genres.map { it.toDomainModel() }
            with(movie) {
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
                    genres,
                    isFavourite
                )
            }
        }

    override suspend fun switchMovieFavouriteFlag(movieId: Long, toFavourite: Boolean): Error? =
        try {
            movieDao.switchFavouriteFlagOfMovie(movieId, toFavourite)
            null
        } catch (e: Exception) {
            FavouriteMovieSQLException(e).toError()
        }

    override suspend fun isMovieFlaggedAsFavourite(movieId: Long): Either<Error, Boolean> =
        try {
            movieDao.isMovieFlaggedAsFavourite(movieId).right()
        } catch (e: Exception) {
            e.toError().left()
        }
}
