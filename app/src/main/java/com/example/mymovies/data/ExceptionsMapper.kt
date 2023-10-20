package com.example.mymovies.data

import android.database.sqlite.SQLiteException
import com.example.mymovies.data.database.FavouriteMovieSQLException
import com.example.mymovies.data.webservice.NoInternetConnectionException
import com.example.mymovies.domain.Error
import retrofit2.HttpException

fun Throwable.toError(): Error =
    when (this) {
        is NoInternetConnectionException -> Error.NoInternetConnection
        is HttpException -> Error.RemoteWebService(code())
        is SQLiteException -> Error.LocalDatabase
        is FavouriteMovieSQLException -> Error.FavouriteMovieDatabase
        else -> Error.Unknown(message ?: "")
    }
