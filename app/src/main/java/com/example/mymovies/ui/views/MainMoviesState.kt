package com.example.mymovies.ui.views

import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController

fun Fragment.buildMainMoviesState(navController: NavController = findNavController()): MainMoviesState =
    MainMoviesState(navController)

class MainMoviesState(private val navController: NavController) {

    fun onMovieClicked(movieId: Int) {
        navController.navigate(MainFragmentDirections.actionMainDestToMovieDetailsDest(movieId))
    }
}