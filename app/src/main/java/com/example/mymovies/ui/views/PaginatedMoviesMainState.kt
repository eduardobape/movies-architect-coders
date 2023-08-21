package com.example.mymovies.ui.views

import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController


class PaginatedMoviesMainState(private val navController: NavController) {
    fun onMovieClicked(movieId: Long) {
        navController.navigate(PaginatedMoviesMainFragmentDirections.actionMainDestToMovieDetailsDest(movieId))
    }

}

fun Fragment.buildPaginatedMoviesMainState(navController: NavController = findNavController()): PaginatedMoviesMainState =
    PaginatedMoviesMainState(navController)
