package com.example.mymovies.framework.database

import com.example.mymovies.data.datasources.MoviesLocalDataSource
import com.example.mymovies.domain.ItemsPaginationInfo
import com.example.mymovies.domain.Movie
import com.example.mymovies.domain.PaginatedMovies
import com.example.mymovies.domain.toMovieDatabaseModel
import com.example.mymovies.domain.toMovieGenreDatabaseModel
import com.example.mymovies.domain.toMoviesPaginationInfoDatabaseModel
import com.example.mymovies.framework.database.daos.MovieDao
import com.example.mymovies.framework.database.entities.MovieWithGenres
import com.example.mymovies.framework.database.entities.toMovieDomainModel
import com.example.mymovies.framework.database.entities.toMovieGenreDomainModel
import com.example.mymovies.framework.database.entities.toPaginationInfoDomainModel
import com.example.mymovies.framework.errors.FavouriteMovieSQLException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MoviesRoomDataSource(private val movieDao: MovieDao) : MoviesLocalDataSource {

    override val allCachedMovies: Flow<List<Movie>>
        get() = movieDao.getAllMovies().map { movies ->
            movies.map { it.toMovieDomainModel() }
        }

    override suspend fun savePaginatedMovies(paginatedMovies: PaginatedMovies) {
        movieDao.savePaginatedMovies(
            paginatedMovies.movies.map { it.toMovieDatabaseModel() },
            paginatedMovies.paginationInfo.toMoviesPaginationInfoDatabaseModel()
        )
    }

    override suspend fun getMoviesPaginationInfo(): ItemsPaginationInfo? {
        return movieDao.getMoviesPaginationInfo()?.toPaginationInfoDomainModel()
    }

    override suspend fun saveMovieDetails(movie: Movie) {
        movieDao.saveMovie(movie.toMovieDatabaseModel())
        movieDao.saveMovieGenresAndRelationship(movie.id, movie.genres.map { it.toMovieGenreDatabaseModel() })
    }

    override fun getMovieWithGenres(movieId: Long): Flow<Movie> = movieDao.findMovie(movieId).map { movieDb: MovieWithGenres ->
        val movie = movieDb.movie.toMovieDomainModel()
        val genres = movieDb.genres.map { it.toMovieGenreDomainModel() }
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

    override suspend fun switchMovieFavouriteFlag(movieId: Long, toFavourite: Boolean) {
        runCatching {
            movieDao.switchFavouriteFlagOfMovie(movieId, toFavourite)
        }.onFailure {
            throw FavouriteMovieSQLException()
        }
    }

    override suspend fun isMovieFlaggedAsFavourite(movieId: Long): Boolean = movieDao.isMovieFlaggedAsFavourite(movieId)
}
