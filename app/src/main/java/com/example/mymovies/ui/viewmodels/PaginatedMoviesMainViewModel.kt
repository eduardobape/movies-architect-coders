package com.example.mymovies.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mymovies.domain.usecases.GetPaginatedMoviesMainUseCase
import com.example.mymovies.ui.models.MoviesSearchFilters
import com.example.mymovies.ui.views.PaginatedMoviesMainUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PaginatedMoviesMainViewModel(
    private val getPaginatedMoviesMainUseCase: GetPaginatedMoviesMainUseCase) : ViewModel() {

    companion object {
        private const val MAX_MOVIES_TO_LOAD = 100
        const val PAGE_SIZE = 20
    }

    private val _uiState: MutableStateFlow<PaginatedMoviesMainUiState> = MutableStateFlow(PaginatedMoviesMainUiState())
    val uiState: StateFlow<PaginatedMoviesMainUiState> = _uiState.asStateFlow()

    init {
        collectPopularMoviesFlow()
    }

    private fun collectPopularMoviesFlow() {
        viewModelScope.launch {
            getPaginatedMoviesMainUseCase.popularMoviesFlow.collectLatest {
                _uiState.update { uiState ->
                    uiState.copy(
                        isLoading = false,
                        movies = it.movies,
                        currentPage = it.page,
                        totalPages = it.pages
                    )
                }
                if (!isFirstPageLoaded())
                    fetchPaginatedMovies()
            }
        }
    }

    private fun isFirstPageLoaded() = uiState.value.currentPage > 0

    fun fetchPaginatedMovies() {
        if (canLoadMovies()) {
            viewModelScope.launch {
                _uiState.update {
                    it.copy(isLoading = true)
                }
                val moviesFilters: MoviesSearchFilters = uiState.value.moviesSearchFilters
                getPaginatedMoviesMainUseCase(moviesFilters, uiState.value.currentPage + 1)
            }
        }
    }

    private fun canLoadMovies(): Boolean = with(uiState.value) {
        !isLoading && (currentPage == 0 || currentPage < totalPages && MAX_MOVIES_TO_LOAD > currentPage * PAGE_SIZE)
    }


    class Factory(
        private val getPaginatedMoviesMainUseCase: GetPaginatedMoviesMainUseCase) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T = PaginatedMoviesMainViewModel(getPaginatedMoviesMainUseCase) as T
    }
}
