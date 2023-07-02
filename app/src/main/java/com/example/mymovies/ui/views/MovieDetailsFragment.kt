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
import com.example.mymovies.data.repository.MovieDetailsRepositoryImpl
import com.example.mymovies.databinding.FragmentMovieDetailsBinding
import com.example.mymovies.domain.models.MovieDetails
import com.example.mymovies.domain.usecases.GetUrlMovieBackdropUseCase
import com.example.mymovies.domain.usecases.MovieDetailsUseCase
import com.example.mymovies.ui.extensions.launchAndCollectFlow
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
            MovieDetailsUseCase(
                MovieDetailsRepositoryImpl(MoviesApi(RetrofitServiceBuilder).movieDetailsApiService)
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
        viewLifecycleOwner.launchAndCollectFlow(viewModel.uiState, ::updateViewsFromUiState)
    }

    private fun updateViewsFromUiState(uiState: MovieDetailsUiState) {
        binding.pbMovieDetails.visible = uiState.isLoading
        if (uiState.isError) {
            Toast.makeText(requireContext(), getString(R.string.movie_id_intent_error), Toast.LENGTH_SHORT).show()
        } else {
            uiState.movieDetails?.let {
                displayMovieDetails(it)
                binding.nesScrollMovieDetails.visible = !uiState.isLoading
            }
        }
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
