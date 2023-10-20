package com.example.mymovies.ui.paginatedmovies

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymovies.R
import com.example.mymovies.appContext
import com.example.mymovies.data.MoviesRepository
import com.example.mymovies.data.database.MoviesRoomDataSource
import com.example.mymovies.data.webservice.MoviesWebServiceDataSource
import com.example.mymovies.data.webservice.RemoteConnection
import com.example.mymovies.databinding.FragmentPaginatedMoviesMainBinding
import com.example.mymovies.domain.Error
import com.example.mymovies.domain.MovieImageSize
import com.example.mymovies.ui.shared.SpacedItemDecoration
import com.example.mymovies.ui.shared.collectFlowWithDiffing
import com.example.mymovies.ui.shared.launchAndCollectFlow
import com.example.mymovies.ui.shared.viewLifecycleBinding
import com.example.mymovies.ui.shared.visible
import com.example.mymovies.usescases.BuildMovieImageUrlUseCase
import com.example.mymovies.usescases.GetCachedPaginatedMoviesUseCase
import com.example.mymovies.usescases.RequestMoviesPageUseCase
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.flow.map

class PaginatedMoviesMainFragment : Fragment(R.layout.fragment_paginated_movies_main) {

    companion object {
        private const val GRIDLAYOUT_COLUMNS_SPACE = 50
    }

    private val binding: FragmentPaginatedMoviesMainBinding by viewLifecycleBinding {
        FragmentPaginatedMoviesMainBinding.bind(requireView())
    }
    private lateinit var moviesAdapter: PaginatedMoviesMainAdapter
    private lateinit var paginatedMoviesMainState: PaginatedMoviesMainState
    private val viewModel by viewModels<PaginatedMoviesMainViewModel> {
        val repository = MoviesRepository(
            MoviesWebServiceDataSource(RemoteConnection.moviesApiServices),
            MoviesRoomDataSource(requireActivity().appContext.database.movieDao())
        )
        PaginatedMoviesMainViewModel.Factory(
            RequestMoviesPageUseCase(repository),
            GetCachedPaginatedMoviesUseCase(repository),
            BuildMovieImageUrlUseCase(repository, MovieImageSize.WIDTH_342PX)
        )
    }
    private val numColumnsMoviesList = 2

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        paginatedMoviesMainState = buildPaginatedMoviesMainState()
        configMoviesAdapter()
        handleUiStateChanges()
        onScrollMovies()
    }

    private fun configMoviesAdapter() {
        moviesAdapter = PaginatedMoviesMainAdapter { movieId ->
            paginatedMoviesMainState.onMovieClicked(movieId)
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

    private fun handleUiStateChanges() {
        handleLoadingUiStateChanges()
        handleMovieInfoUiStateChanges()
        handleErrorUiStateChanges()
    }

    private fun handleLoadingUiStateChanges() {
        viewModel.uiState.collectFlowWithDiffing(
            viewLifecycleOwner,
            { uiState -> uiState.isLoading },
            { isVisible ->
                binding.pbMoviesList.visible = isVisible
                moviesAdapter.changeActivationOnClickMovie(!isVisible)
            }
        )
    }

    private fun handleMovieInfoUiStateChanges() {
        viewModel.uiState.collectFlowWithDiffing(
            viewLifecycleOwner,
            { uiState -> uiState.movies },
            { moviesAdapter.submitList(it) }
        )
    }

    private fun handleErrorUiStateChanges() {
        viewLifecycleOwner.launchAndCollectFlow(
            viewModel.uiState.map { it.error },
            { error ->
                error?.let {
                    moviesAdapter.changeActivationOnClickMovie(false)
                    displayError(it)
                    viewModel.notifyErrorShown()
                }
            }
        )
    }

    private fun displayError(error: Error) {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage("${paginatedMoviesMainState.getErrorMessage(error)}\n${getString(R.string.try_again)}")
            .setCancelable(false)
            .setPositiveButton(getString(R.string.try_again_button_dialog)) { _, _ ->
                viewModel.fetchPaginatedMovies()
            }
            .setNegativeButton(getString(R.string.default_negative_button_dialog)) { _, _ ->
                moviesAdapter.changeActivationOnClickMovie(true)
            }
            .show()
    }

    private fun onScrollMovies() {
        binding.rvMoviesList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val isDownVerticalScroll = dy > 0
                if (isDownVerticalScroll) {
                    val layoutManager: LinearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
                    if (layoutManager.findLastCompletelyVisibleItemPosition() + (numColumnsMoviesList - 1) ==
                        layoutManager.itemCount
                    ) {
                        viewModel.fetchPaginatedMovies()
                    }
                }
            }
        })
    }
}
