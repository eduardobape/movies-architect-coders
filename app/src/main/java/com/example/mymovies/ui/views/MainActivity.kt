package com.example.mymovies.ui.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymovies.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

	private lateinit var binding: ActivityMainBinding
	private val moviesAdapter = MoviesAdapter()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityMainBinding.inflate(layoutInflater)
		setContentView(binding.root)
		setUpMoviesAdapter()
	}

	private fun setUpMoviesAdapter() {
		val recyclerViewMovies = binding.rvMoviesList
		recyclerViewMovies.adapter = moviesAdapter
		recyclerViewMovies.layoutManager = GridLayoutManager(this, 2, RecyclerView.VERTICAL, false)
		recyclerViewMovies.addItemDecoration(SpacesItemDecoration(2, 50, true))
	}
}