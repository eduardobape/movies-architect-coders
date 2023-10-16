package com.example.mymovies.framework.database

import com.example.mymovies.data.datasources.MoviesLocalDataSource
import com.example.mymovies.domain.ItemsPaginationInfo
import com.example.mymovies.domain.Movie
import com.example.mymovies.domain.PaginatedMovies
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MoviesRoomDataSource(private val movieDao: MovieDao) : MoviesLocalDataSource {

    override val allCachedMovies: Flow<List<Movie>>
        get() = movieDao.getAllMovies().map { movies ->
            movies.map { it.toDomainModel() }
        }

    override suspend fun savePaginatedMovies(paginatedMovies: PaginatedMovies) {
        movieDao.savePaginatedMovies(
            paginatedMovies.movies.map { it.toDatabaseModel() },
            paginatedMovies.paginationInfo.toMoviesPaginationInfoDatabaseModel()
        )
    }

    override suspend fun getMoviesPaginationInfo(): ItemsPaginationInfo? {
        return movieDao.getMoviesPaginationInfo()?.toDomainModel()
    }

    override suspend fun saveMovieDetails(movie: Movie) {
        movieDao.saveMovie(movie.toDatabaseModel())
        movieDao.saveMovieGenresAndRelationship(movie.id, movie.genres.map { it.toDatabaseModel() })
    }

    override fun getMovieWithGenres(movieId: Long): Flow<Movie> = movieDao.findMovie(movieId).map { movieDb: MovieWithGenres ->
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

    override suspend fun switchMovieFavouriteFlag(movieId: Long, toFavourite: Boolean) {
        runCatching {
            movieDao.switchFavouriteFlagOfMovie(movieId, toFavourite)
        }.onFailure {
            throw FavouriteMovieSQLException()
        }
    }

    override suspend fun isMovieFlaggedAsFavourite(movieId: Long): Boolean = movieDao.isMovieFlaggedAsFavourite(movieId)
}
