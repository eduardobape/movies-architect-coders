package com.example.mymovies.ui.views

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymovies.data.remote.client.RetrofitServiceBuilder
import com.example.mymovies.data.remote.services.MoviesApi
import com.example.mymovies.data.repository.MoviesDiscoveryRepositoryImpl
import com.example.mymovies.databinding.ActivityMainBinding
import com.example.mymovies.domain.models.MovieMainDetails
import com.example.mymovies.domain.usecases.GetPopularMoviesUseCase
import com.example.mymovies.ui.utils.startActivity
import com.example.mymovies.ui.utils.visible
import com.example.mymovies.ui.viewmodels.MainViewModel

class MainActivity : AppCompatActivity() {

	companion object {
		private const val GRIDLAYOUT_COLUMNS_SPACE = 50
	}

	private lateinit var binding: ActivityMainBinding
	private lateinit var moviesAdapter: MoviesAdapter
	private val viewModel by viewModels<MainViewModel> {
		MainViewModel.Factory(
			GetPopularMoviesUseCase(MoviesDiscoveryRepositoryImpl(
				MoviesApi(RetrofitServiceBuilder).moviesDiscoveryApiService)
			)
		)
	}
	private val numColumnsMoviesList = 2

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityMainBinding.inflate(layoutInflater)
		setContentView(binding.root)
		configMoviesAdapter()
		hookToUiState()
		onScrollMovies()
	}

	private fun configMoviesAdapter() {
		moviesAdapter = MoviesAdapter { movieId ->
			startActivity<MovieDetailsActivity>(bundleOf(MovieDetailsActivity.MOVIE_ID to movieId))
		}
		with(binding.rvMoviesList) {
			adapter = moviesAdapter
			layoutManager = GridLayoutManager(
				this@MainActivity,
				numColumnsMoviesList,
				RecyclerView.VERTICAL,
				false
			)
			addItemDecoration(SpacedItemDecoration(numColumnsMoviesList, GRIDLAYOUT_COLUMNS_SPACE, true))
		}
	}

	private fun hookToUiState() {
		viewModel.uiState.observe(this, ::updateViewsFromUiState)
	}

	private fun updateViewsFromUiState(uiState: MoviesDiscoveryState) {
		when (uiState.moviesLoadState) {
			is MoviesLoadState.Success -> {
				val movies: List<MovieMainDetails>? = uiState.moviesDiscoveryDetails?.movies
				movies?.let { submitMoviesToAdapter(it) }
				binding.pbMoviesList.visible = false
			}

			is MoviesLoadState.Loading -> binding.pbMoviesList.visible = true

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

	private fun submitMoviesToAdapter(movies: List<MovieMainDetails>) {
		moviesAdapter.submitList(movies)
	}

	private fun onScrollMovies() {
		binding.rvMoviesList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
			override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
				super.onScrolled(recyclerView, dx, dy)
				val isDownVerticalScroll = dy > 0
				if (viewModel.getState() !is MoviesLoadState.Loading &&
					viewModel.getState() !is MoviesLoadState.ExhaustedPagination &&
					isDownVerticalScroll
				) {
					val layoutManager: LinearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
					val visibleItemCount = layoutManager.childCount
					val totalItemCount = layoutManager.itemCount
					val firstVisibleItemPosition =
						layoutManager.findFirstVisibleItemPosition()
					if (visibleItemCount + firstVisibleItemPosition >= totalItemCount) {
						viewModel.getMovies(viewModel.moviesFilters)
					}
				}
			}
		})
	}

	override fun onRestoreInstanceState(savedInstanceState: Bundle) {
		reloadMovies()
		super.onRestoreInstanceState(savedInstanceState)
	}

	private fun reloadMovies() {
		// The movies list must be submitted to the adapter when the activity is recreated because
		// of a configuration change. If not, the movies will not be displayed.
		val movies: List<MovieMainDetails>? =
			viewModel.uiState.value?.moviesDiscoveryDetails?.movies
		movies?.let {
			submitMoviesToAdapter(it)
		}
	}
}
