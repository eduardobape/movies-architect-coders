package com.example.mymovies.framework

import android.database.sqlite.SQLiteException
import com.example.mymovies.domain.Error
import com.example.mymovies.framework.database.FavouriteMovieSQLException
import com.example.mymovies.framework.webservice.NoInternetConnectionException
import retrofit2.HttpException

fun Throwable.toError(): Error =
    when (this) {
        is NoInternetConnectionException -> Error.NoInternetConnection
        is HttpException -> Error.RemoteWebService(code())
        is SQLiteException -> Error.LocalDatabase
        is FavouriteMovieSQLException -> Error.FavouriteMovieDatabase
        else -> Error.Unknown(message ?: "")
    }
