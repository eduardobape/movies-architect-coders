package com.example.mymovies.framework.errors

import android.database.sqlite.SQLiteException
import com.example.mymovies.domain.Error
import retrofit2.HttpException
import java.io.IOException

fun Throwable.toError(): Error =
    when (this) {
        is NoInternetConnectionException -> Error.NoInternetConnection
        is HttpException -> Error.RemoteWebService(code())
        is SQLiteException -> Error.LocalDatabase
        is FavouriteMovieSQLException -> Error.FavouriteMovieDatabase
        else -> Error.Unknown(message ?: "")
    }

class NoInternetConnectionException(message: String) : IOException(message)

class FavouriteMovieSQLException : Exception()
