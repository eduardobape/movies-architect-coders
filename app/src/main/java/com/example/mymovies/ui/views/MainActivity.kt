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
	private lateinit var moviesAdapter: MoviesAdapter
	private lateinit var moviesLayoutManager: GridLayoutManager
	private val viewModel by viewModels<MainViewModel> {
		MainViewModel.Factory(
			DiscoverMoviesUseCase(MoviesDiscoveryRepositoryImpl(MoviesApi.moviesDiscoveryApiService))
		)
	}
	private var isLoadingMovies = false
	private var areMoreMoviesAvailable = false
	private var moviesListPage = 1

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityMainBinding.inflate(layoutInflater)
		setContentView(binding.root)

		configMoviesAdapter()
		onScrollMovies()
		updateMoviesList()
		viewModel.getMovies(2023, "ES", "ES_es", "release_date.desc", moviesListPage)
	}

	private fun configMoviesAdapter() {
		moviesAdapter = MoviesAdapter()
		with(binding.rvMoviesList) {
			adapter = moviesAdapter
			GridLayoutManager(this@MainActivity, 2, RecyclerView.VERTICAL, false).also {
				layoutManager = it
				this@MainActivity.moviesLayoutManager = it
			}
			addItemDecoration(SpacesItemDecoration(2, 50, true))
		}
	}

	private fun updateMoviesList() {
		viewModel.movies.observe(this) {
			areMoreMoviesAvailable = it.isNotEmpty()
			moviesAdapter.submitList(moviesAdapter.currentList + it)
			isLoadingMovies = false
		}
	}

	private fun onScrollMovies() {
		binding.rvMoviesList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
			override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
				super.onScrolled(recyclerView, dx, dy)
				val visibleItemCount = moviesLayoutManager.childCount
				val totalItemCount = moviesLayoutManager.itemCount
				val firstVisibleItemPosition = moviesLayoutManager.findFirstVisibleItemPosition()

				if (!isLoadingMovies && areMoreMoviesAvailable && dy > 0 &&
					(visibleItemCount + firstVisibleItemPosition) >= totalItemCount) {
					isLoadingMovies = true
					moviesListPage++
					viewModel.getMovies(2023, "ES", "ES_es", "release_date.desc", moviesListPage)
				}
			}
		})
	}
}