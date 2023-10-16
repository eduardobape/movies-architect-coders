package com.example.mymovies.ui.paginatedmovies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mymovies.domain.Error
import com.example.mymovies.domain.MoviesSearchFilters
import com.example.mymovies.framework.errors.toError
import com.example.mymovies.usecases.BuildMovieImageUrlUseCase
import com.example.mymovies.usecases.GetCachedPaginatedMoviesUseCase
import com.example.mymovies.usecases.RequestMoviesPageUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PaginatedMoviesMainViewModel(
    private val requestMoviesPageUseCase: RequestMoviesPageUseCase,
    private val getCachedPaginatedMoviesUseCase: GetCachedPaginatedMoviesUseCase,
    private val buildMovieImageUrlUseCase: BuildMovieImageUrlUseCase
) : ViewModel() {

    companion object {
        private const val MAX_MOVIES_TO_LOAD = 100
        private const val PAGE_SIZE = 20
    }

    private val _uiState: MutableStateFlow<PaginatedMoviesMainUiState> = MutableStateFlow(PaginatedMoviesMainUiState())
    val uiState: StateFlow<PaginatedMoviesMainUiState> = _uiState.asStateFlow()

    init {
        collectPopularMoviesFlow()
    }

    private fun collectPopularMoviesFlow() {
        viewModelScope.launch {
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
                            movies = it.movies.map { movie ->
                                movie.posterImageRelativePath?.let {
                                    val moviePosterImageUrl = buildMovieImageUrlUseCase(it)
                                    movie.toPaginatedMovieUiModel().copy(posterUrl = moviePosterImageUrl)
                                } ?: movie.toPaginatedMovieUiModel()
                            },
                            paginationInfo = it.paginationInfo,
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

    private fun isFirstPageLoaded() = uiState.value.paginationInfo.lastPageLoaded > 0

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
                    requestMoviesPageUseCase(moviesFilters, uiState.value.paginationInfo.lastPageLoaded + 1)
                error?.let {
                    _uiState.update {
                        it.copy(error = error, isLoading = false, isNeededRetryFetchMovies = true)
                    }
                }
            }
        }
    }

    private fun canLoadMovies(): Boolean = with(uiState.value) {
        !isLoading && (paginationInfo.lastPageLoaded == 0 ||
                paginationInfo.lastPageLoaded < paginationInfo.totalPages &&
                MAX_MOVIES_TO_LOAD > paginationInfo.lastPageLoaded * PAGE_SIZE)
    }

    fun notifyErrorShown() {
        _uiState.update {
            it.copy(error = null)
        }
    }


    class Factory(
        private val requestMoviesPageUseCase: RequestMoviesPageUseCase,
        private val getCachedPaginatedMoviesUseCase: GetCachedPaginatedMoviesUseCase,
        private val buildMovieImageUrlUseCase: BuildMovieImageUrlUseCase
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            PaginatedMoviesMainViewModel(
                requestMoviesPageUseCase,
                getCachedPaginatedMoviesUseCase,
                buildMovieImageUrlUseCase
            ) as T
    }
}
