package com.example.mymovies.data.errors

import android.database.sqlite.SQLiteException
import retrofit2.HttpException
import java.io.IOException

class NoInternetConnectionException(message: String) : IOException(message)

class FavouriteMovieSQLException : Exception()

sealed interface Error {
    object NoInternetConnection : Error
    class RemoteWebService(val httpErrorCode: Int) : Error
    object LocalDatabase : Error
    object FavouriteMovieDatabase : Error
    class Unknown(val errorMessage: String) : Error
}

fun Throwable.toError(): Error =
    when (this) {
        is NoInternetConnectionException -> Error.NoInternetConnection
        is HttpException -> Error.RemoteWebService(code())
        is SQLiteException -> Error.LocalDatabase
        is FavouriteMovieSQLException -> Error.FavouriteMovieDatabase
        else -> Error.Unknown(message ?: "")
    }
