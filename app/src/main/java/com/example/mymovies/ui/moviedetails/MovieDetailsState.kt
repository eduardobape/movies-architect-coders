package com.example.mymovies.ui.moviedetails

import android.content.Context
import androidx.fragment.app.Fragment
import com.example.mymovies.R
import com.example.mymovies.domain.Error

class MovieDetailsState(private val context: Context) {

    fun getErrorMessage(error: Error): String = when(error) {
        is Error.NoInternetConnection -> context.getString(R.string.error_internet_connection_movie_details)
        is Error.LocalDatabase -> context.getString(R.string.error_database_movie_details)
        is Error.RemoteWebService -> context.getString(
            R.string.error_webservice_fetch_movie_details,
            error.httpErrorCode.toString()
        )
        is Error.Unknown -> context.getString(R.string.error_unexpected_movie_details)
        is Error.FavouriteMovieDatabase -> context.getString(R.string.error_saving_movie_as_favourite_database)
    }
}

fun Fragment.buildMovieDetailsState(): MovieDetailsState = MovieDetailsState(requireContext())
