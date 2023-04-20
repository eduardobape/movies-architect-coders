package com.example.mymovies.ui.views

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymovies.data.remoteapi.services.MoviesApi
import com.example.mymovies.data.repository.MoviesDiscoveryRepositoryImpl
import com.example.mymovies.databinding.ActivityMainBinding
import com.example.mymovies.domain.usecases.DiscoverMoviesUseCase
import com.example.mymovies.ui.viewmodels.MainViewModel

class MainActivity : AppCompatActivity() {

	private lateinit var binding: ActivityMainBinding
	private val moviesAdapter = MoviesAdapter()
	private val viewModel by viewModels<MainViewModel> {
		MainViewModel.Factory(
			DiscoverMoviesUseCase(MoviesDiscoveryRepositoryImpl(MoviesApi.moviesDiscoveryApiService))
		)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityMainBinding.inflate(layoutInflater)
		setContentView(binding.root)
		setUpMoviesAdapter()
		updateMoviesList()
	}

	private fun setUpMoviesAdapter() {
		with (binding.rvMoviesList) {
			adapter = moviesAdapter
			layoutManager = GridLayoutManager(this@MainActivity, 2, RecyclerView.VERTICAL, false)
			addItemDecoration(SpacesItemDecoration(2, 50, true))
		}
	}

	private fun updateMoviesList() {
		viewModel.movies.observe(this) {
			moviesAdapter.submitList(it)
		}
	}
}