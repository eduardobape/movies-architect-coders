package com.example.mymovies.ui.views

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymovies.R
import com.example.mymovies.data.remote.client.RetrofitServiceBuilder
import com.example.mymovies.data.remote.services.MoviesApi
import com.example.mymovies.data.repository.MoviesDiscoveryRepositoryImpl
import com.example.mymovies.databinding.FragmentMainBinding
import com.example.mymovies.domain.usecases.GetPopularMoviesUseCase
import com.example.mymovies.ui.extensions.launchAndCollectFlow
import com.example.mymovies.ui.extensions.viewLifecycleBinding
import com.example.mymovies.ui.extensions.visible
import com.example.mymovies.ui.viewmodels.MainViewModel

class MainFragment : Fragment(R.layout.fragment_main) {

    companion object {
        private const val GRIDLAYOUT_COLUMNS_SPACE = 50
    }

    private val binding: FragmentMainBinding by viewLifecycleBinding {
        FragmentMainBinding.bind(requireView())
    }
    private lateinit var moviesAdapter: MainMoviesAdapter
    private lateinit var mainMoviesState: MainMoviesState
    private val viewModel by viewModels<MainViewModel> {
        MainViewModel.Factory(
            GetPopularMoviesUseCase(
                MoviesDiscoveryRepositoryImpl(
                    MoviesApi(RetrofitServiceBuilder).moviesDiscoveryApiService
                )
            )
        )
    }
    private val numColumnsMoviesList = 2

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainMoviesState = buildMainMoviesState()
        configMoviesAdapter()
        hookToUiState()
        onScrollMovies()
    }

    private fun configMoviesAdapter() {
        moviesAdapter = MainMoviesAdapter { movieId ->
            mainMoviesState.onMovieClicked(movieId)
        }
        with(binding.rvMoviesList) {
            adapter = moviesAdapter
            layoutManager = GridLayoutManager(
                context,
                numColumnsMoviesList,
                RecyclerView.VERTICAL,
                false
            )
            addItemDecoration(SpacedItemDecoration(numColumnsMoviesList, GRIDLAYOUT_COLUMNS_SPACE, true))
        }
    }

    private fun hookToUiState() {
        viewLifecycleOwner.launchAndCollectFlow(viewModel.uiState, ::updateViewsFromUiState)
    }

    private fun updateViewsFromUiState(uiState: MainMoviesUiState) {
        moviesAdapter.submitList(uiState.moviesDiscoveryDetails.movies)
        binding.pbMoviesList.visible = uiState.isLoading
    }

    private fun onScrollMovies() {
        binding.rvMoviesList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val isDownVerticalScroll = dy > 0
                if (isDownVerticalScroll && !viewModel.uiState.value.isLoading
                ) {
                    val layoutManager: LinearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                    if (visibleItemCount + firstVisibleItemPosition >= totalItemCount) {
                        viewModel.getMovies()
                    }
                }
            }
        })
    }
}
