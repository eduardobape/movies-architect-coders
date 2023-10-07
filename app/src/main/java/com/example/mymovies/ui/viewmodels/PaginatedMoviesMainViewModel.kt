package com.example.mymovies.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mymovies.data.errors.Error
import com.example.mymovies.data.errors.toError
import com.example.mymovies.data.repositories.PaginatedMoviesRepository
import com.example.mymovies.domain.usecases.GetCachedPaginatedMoviesUseCase
import com.example.mymovies.domain.usecases.RequestMoviesPageUseCase
import com.example.mymovies.ui.models.MoviesSearchFilters
import com.example.mymovies.ui.views.PaginatedMoviesMainUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PaginatedMoviesMainViewModel(
    private val requestMoviesPageUseCase: RequestMoviesPageUseCase,
    private val getCachedPaginatedMoviesUseCase: GetCachedPaginatedMoviesUseCase
) : ViewModel() {

    companion object {
        private const val MAX_MOVIES_TO_LOAD = 100
    }

    private val _uiState: MutableStateFlow<PaginatedMoviesMainUiState> = MutableStateFlow(PaginatedMoviesMainUiState())
    val uiState: StateFlow<PaginatedMoviesMainUiState> = _uiState.asStateFlow()

    init {
        collectPopularMoviesFlow()
    }

    private fun collectPopularMoviesFlow(): Job {
        return viewModelScope.launch {
            getCachedPaginatedMoviesUseCase()
                .catch { exception ->
                    _uiState.update {
                        it.copy(
                            error = exception.toError(),
                            isLoading = false,
                            isNeededRetryCollectMovies = true
                        )
                    }
                }
                .collect {
                    _uiState.update { uiState ->
                        uiState.copy(
                            isLoading = false,
                            movies = it.movies,
                            currentPage = it.page,
                            totalPages = it.pages,
                            error = null,
                            isNeededRetryCollectMovies = false,
                            isNeededRetryFetchMovies = false
                        )
                    }
                    if (!isFirstPageLoaded())
                        fetchPaginatedMovies()
                }
        }
    }

    private fun isFirstPageLoaded() = uiState.value.currentPage > 0

    fun fetchPaginatedMovies() {
        viewModelScope.launch {
            if (uiState.value.isNeededRetryCollectMovies) {
                _uiState.update {
                    it.copy(
                        isLoading = true,
                        error = null,
                        isNeededRetryFetchMovies = false,
                        isNeededRetryCollectMovies = false
                    )
                }
                collectPopularMoviesFlow()
            } else if (canLoadMovies()) {
                _uiState.update {
                    it.copy(
                        isLoading = true,
                        error = null,
                        isNeededRetryFetchMovies = false,
                        isNeededRetryCollectMovies = false
                    )
                }
                val moviesFilters: MoviesSearchFilters = uiState.value.moviesSearchFilters
                val error: Error? =
                    requestMoviesPageUseCase(moviesFilters, uiState.value.currentPage + 1)
                error?.let {
                    _uiState.update {
                        it.copy(error = error, isLoading = false, isNeededRetryFetchMovies = true)
                    }
                }
            }
        }
    }

    private fun canLoadMovies(): Boolean = with(uiState.value) {
        !isLoading && (currentPage == 0 ||
                currentPage < totalPages && MAX_MOVIES_TO_LOAD > currentPage * PaginatedMoviesRepository.PAGE_SIZE)
    }

    fun notifyErrorShown() {
        _uiState.update {
            it.copy(error = null)
        }
    }


    class Factory(
        private val requestMoviesPageUseCase: RequestMoviesPageUseCase,
        private val getCachedPaginatedMoviesUseCase: GetCachedPaginatedMoviesUseCase
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            PaginatedMoviesMainViewModel(requestMoviesPageUseCase, getCachedPaginatedMoviesUseCase) as T
    }
}
