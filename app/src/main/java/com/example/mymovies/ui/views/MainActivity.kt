package com.example.mymovies.ui.views

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymovies.data.remoteapi.services.MoviesApi
import com.example.mymovies.data.repository.MoviesDiscoveryRepositoryImpl
import com.example.mymovies.databinding.ActivityMainBinding
import com.example.mymovies.domain.usecases.DiscoverMoviesUseCase
import com.example.mymovies.ui.utils.startActivity
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

	companion object {
		const val MOVIE_ID_INTENT_EXTRA_KEY = "movieID"
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityMainBinding.inflate(layoutInflater)
		setContentView(binding.root)
		configMoviesAdapter()
		onScrollMovies()
		updateMovies()
	}

	private fun configMoviesAdapter() {
        moviesAdapter = MoviesAdapter { movieId ->
            startActivity<MovieDetailActivity>(bundleOf(MOVIE_ID_INTENT_EXTRA_KEY to movieId))
        }
		with(binding.rvMoviesList) {
			adapter = moviesAdapter
			GridLayoutManager(this@MainActivity, 2, RecyclerView.VERTICAL, false).also {
				layoutManager = it
				this@MainActivity.moviesLayoutManager = it
			}
			addItemDecoration(SpacedItemDecoration(2, 50, true))
		}
	}

	private fun updateMovies() {
		viewModel.uiState.observe(this) { uiState ->
			when (uiState.moviesLoadState) {
				is MoviesLoadState.Success -> {
					moviesAdapter.submitList(uiState.moviesDiscoveryDetails?.movies)
				}

				is MoviesLoadState.Loading -> Toast.makeText(
					this,
					"Loading",
					Toast.LENGTH_SHORT
				).show()

				is MoviesLoadState.Error -> Toast.makeText(
					this,
					uiState.moviesLoadState.errorMessage,
					Toast.LENGTH_SHORT
				).show()

				MoviesLoadState.ExhaustedPagination -> {
					Toast.makeText(this, "No more movies to load", Toast.LENGTH_SHORT).show()
				}
			}
		}
	}

	private fun onScrollMovies() {
		binding.rvMoviesList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
			override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
				super.onScrolled(recyclerView, dx, dy)
				val visibleItemCount = moviesLayoutManager.childCount
				val totalItemCount = moviesLayoutManager.itemCount
				val firstVisibleItemPosition = moviesLayoutManager.findFirstVisibleItemPosition()

				if (viewModel.getState() !is MoviesLoadState.Loading &&
					viewModel.getState() !is MoviesLoadState.ExhaustedPagination &&
					dy > 0 &&
					(visibleItemCount + firstVisibleItemPosition) >= totalItemCount
				) {
					viewModel.getMovies(viewModel.moviesFilters)
				}
			}
		})
	}
}