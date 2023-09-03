package com.example.mymovies.ui.views

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymovies.R
import com.example.mymovies.appContext
import com.example.mymovies.data.repositories.PaginatedMoviesRepository
import com.example.mymovies.databinding.FragmentPaginatedMoviesMainBinding
import com.example.mymovies.domain.usecases.GetPaginatedMoviesMainUseCase
import com.example.mymovies.ui.extensions.collectFlowWithDiffing
import com.example.mymovies.ui.extensions.viewLifecycleBinding
import com.example.mymovies.ui.extensions.visible
import com.example.mymovies.ui.viewmodels.PaginatedMoviesMainViewModel

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
        val paginatedMoviesRepository = PaginatedMoviesRepository(requireContext().appContext)
        PaginatedMoviesMainViewModel.Factory(GetPaginatedMoviesMainUseCase(paginatedMoviesRepository))
    }
    private val numColumnsMoviesList = 2

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        paginatedMoviesMainState = buildPaginatedMoviesMainState()
        configMoviesAdapter()
        hookToUiState()
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

    private fun hookToUiState() {
        with(viewModel.uiState) {
            collectFlowWithDiffing(
                viewLifecycleOwner,
                { uiState -> uiState.isLoading },
                { isVisible -> binding.pbMoviesList.visible = isVisible })
            collectFlowWithDiffing(viewLifecycleOwner, { uiState -> uiState.movies }, { moviesAdapter.submitList(it) })
        }
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
