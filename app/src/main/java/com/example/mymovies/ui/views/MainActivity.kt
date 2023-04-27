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
import com.example.mymovies.ui.models.MoviesDiscoveryFilters
import com.example.mymovies.ui.viewmodels.MainViewModel
import java.time.LocalDate
import java.util.Locale

class MainActivity : AppCompatActivity() {

	private lateinit var binding: ActivityMainBinding
	private lateinit var moviesAdapter: MoviesAdapter
	private lateinit var moviesLayoutManager: GridLayoutManager
	private val viewModel by viewModels<MainViewModel> {
		MainViewModel.Factory(
			DiscoverMoviesUseCase(MoviesDiscoveryRepositoryImpl(MoviesApi.moviesDiscoveryApiService))
		)
	}
	private lateinit var moviesFilters: MoviesDiscoveryFilters
	private var isLoadingMovies = false
	private var areMoreMoviesAvailable = false

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityMainBinding.inflate(layoutInflater)
		setContentView(binding.root)

		configMoviesAdapter()
		initMoviesFilters()
		onScrollMovies()
		updateMoviesList()
		viewModel.getMovies(moviesFilters)
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
		viewModel.moviesDetails.observe(this) { moviesDetails ->
			areMoreMoviesAvailable = moviesDetails.pages > moviesDetails.page
			increaseMoviesPageToLoad()
			moviesAdapter.submitList(moviesAdapter.currentList + moviesDetails.movies)
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
					(visibleItemCount + firstVisibleItemPosition) >= totalItemCount
				) {
					isLoadingMovies = true
					viewModel.getMovies(moviesFilters)
				}
			}
		})
	}

	private fun initMoviesFilters() {
		moviesFilters = MoviesDiscoveryFilters(
			LocalDate.now().year,
			Locale.getDefault().country,
			Locale.getDefault().language,
			"release_date.asc",
			1
		)
	}

	private fun increaseMoviesPageToLoad() {
		moviesFilters.page++
	}
}