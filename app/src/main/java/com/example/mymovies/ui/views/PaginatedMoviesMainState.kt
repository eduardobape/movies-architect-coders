package com.example.mymovies.ui.views

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.mymovies.R
import com.example.mymovies.data.errors.Error


class PaginatedMoviesMainState(private val context: Context, private val navController: NavController) {
    fun onMovieClicked(movieId: Long) {
        navController.navigate(PaginatedMoviesMainFragmentDirections.actionMainDestToMovieDetailsDest(movieId))
    }

    fun getErrorMessage(error: Error): String = when (error) {
        is Error.NoInternetConnection -> context.getString(R.string.error_internet_connection_paginated_movies)
        is Error.LocalDatabase -> context.getString(R.string.error_database_paginated_movies)
        is Error.RemoteWebService -> context.getString(
            R.string.error_webservice_fetch_paginated_movies,
            error.httpErrorCode.toString()
        )
        else -> context.getString(R.string.error_unexpected_paginated_movies)
    }
}

fun Fragment.buildPaginatedMoviesMainState(
    navController: NavController = findNavController()
): PaginatedMoviesMainState =
    PaginatedMoviesMainState(requireContext(), navController)
