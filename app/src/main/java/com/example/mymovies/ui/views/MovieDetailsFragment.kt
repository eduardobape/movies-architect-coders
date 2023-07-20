package com.example.mymovies.ui.views

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.mymovies.R
import com.example.mymovies.data.remote.client.RetrofitServiceBuilder
import com.example.mymovies.data.remote.services.ApiUrlsManager.ApiImageUtils.PosterMovieSize
import com.example.mymovies.data.remote.services.MoviesApi
import com.example.mymovies.data.repositories.MovieDetailsRemoteRepository
import com.example.mymovies.databinding.FragmentMovieDetailsBinding
import com.example.mymovies.domain.models.MovieDetails
import com.example.mymovies.domain.usecases.GetMovieDetailsUseCase
import com.example.mymovies.domain.usecases.GetUrlMovieBackdropUseCase
import com.example.mymovies.ui.extensions.diffingUiState
import com.example.mymovies.ui.extensions.loadImageFromUrl
import com.example.mymovies.ui.extensions.viewLifecycleBinding
import com.example.mymovies.ui.extensions.visible
import com.example.mymovies.ui.viewmodels.MovieDetailsViewModel


class MovieDetailsFragment : Fragment(R.layout.fragment_movie_details) {

    private val binding: FragmentMovieDetailsBinding by viewLifecycleBinding {
        FragmentMovieDetailsBinding.bind(requireView())
    }
    private val args: MovieDetailsFragmentArgs by navArgs()
    private val viewModel by viewModels<MovieDetailsViewModel> {
        MovieDetailsViewModel.Factory(
            GetMovieDetailsUseCase(
                MovieDetailsRemoteRepository(MoviesApi(RetrofitServiceBuilder).movieDetailsApiService)
            ),
            getMovieIdFromSafeArgs()
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hookToUiState()
    }

    private fun getMovieIdFromSafeArgs(): Int {
        return args.movieId
    }

    private fun hookToUiState() {
        manageLoadingUiState()
        manageMoviesUiState()
        manageErrorUiState()
    }

    private fun manageLoadingUiState() {
        viewModel.uiState.diffingUiState(
            viewLifecycleOwner,
            { it.isLoading },
            { binding.pbMovieDetails.visible = it }
        )
    }

    private fun manageMoviesUiState() {
        viewModel.uiState.diffingUiState(
            viewLifecycleOwner,
            { it.movieDetails },
            {
                it?.let { movieDetails ->
                    displayMovieDetails(movieDetails)
                    binding.nesScrollMovieDetails.visible = true
                }
            }
        )
    }

    private fun manageErrorUiState() {
        viewModel.uiState.diffingUiState(
            viewLifecycleOwner,
            { it.isError },
            { isError ->
                if (isError) {
                    Toast.makeText(requireContext(), getString(R.string.movie_id_intent_error), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        )
    }

    private fun displayMovieDetails(movieDetails: MovieDetails) {
        movieDetails.also {
            displayMovieImage(it)
            displayMovieTranslatedTitle(it.translatedTitle)
            displayMovieOriginalTitle(it.originalTitle)
            displayMovieReleaseDate(it.releaseDate)
            displayMovieVoteAverage(it.voteAverage)
            displayMovieGenres(it.genres)
            displayMovieOverview(it.overview.orEmpty())
        }
    }

    private fun displayMovieImage(movieDetails: MovieDetails) {
        val pathMovieImage = movieDetails.backdropImagePath ?: movieDetails.posterPath
        if (pathMovieImage != null) {
            val urlMovieImage = GetUrlMovieBackdropUseCase(pathMovieImage, PosterMovieSize.WIDTH_780PX)
            binding.ivMovieHeaderImage.loadImageFromUrl(urlMovieImage)
        } else {
            binding.ivMovieHeaderImage.setImageDrawable(
                ContextCompat.getDrawable(requireContext(), R.drawable.no_movie_poster)
            )
        }
    }

    private fun displayMovieTranslatedTitle(title: String) {
        binding.tvMovieTranslatedTitle.text = title
    }

    private fun displayMovieOriginalTitle(title: String) {
        binding.tvMovieOriginalTitle.text = title
    }

    private fun displayMovieReleaseDate(releaseDate: String) {
        binding.tvMovieReleaseDate.text = releaseDate
    }

    private fun displayMovieVoteAverage(voteAverage: Float) {
        binding.tvMovieVoteAverage.text = voteAverage.toString()
    }

    private fun displayMovieGenres(movieGenresNames: List<String>) {
        binding.tvMovieGenresNames.text = movieGenresNames.joinToString(separator = ", ")
    }

    private fun displayMovieOverview(movieOverview: String) {
        binding.tvMovieOverview.text = movieOverview.ifEmpty {
            resources.getString(R.string.movie_overview_headline)
        }
    }
}
