package com.example.mymovies.ui.views

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.mymovies.R
import com.example.mymovies.appContext
import com.example.mymovies.data.remote.apiurls.ApiUrlsManager
import com.example.mymovies.data.repositories.MovieDetailsRepository
import com.example.mymovies.databinding.FragmentMovieDetailsBinding
import com.example.mymovies.domain.models.MovieDetails
import com.example.mymovies.domain.usecases.BuildUrlMovieBackdropUseCase
import com.example.mymovies.domain.usecases.GetMovieDetailsUseCase
import com.example.mymovies.domain.usecases.SwitchMovieFavouriteUseCase
import com.example.mymovies.ui.extensions.collectFlowWithDiffing
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
            GetMovieDetailsUseCase(MovieDetailsRepository(requireActivity().appContext), getMovieIdFromSafeArgs()),
            SwitchMovieFavouriteUseCase(MovieDetailsRepository(requireActivity().appContext))
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hookToUiState()
        manageSwitchingFavourite()
    }

    private fun getMovieIdFromSafeArgs(): Long {
        return args.movieId
    }

    private fun hookToUiState() {
        manageLoadingUiState()
        manageMoviesUiState()
    }

    private fun manageLoadingUiState() {
        viewModel.uiState.collectFlowWithDiffing(
            viewLifecycleOwner,
            { it.isLoading },
            { binding.pbMovieDetails.visible = it }
        )
    }

    private fun manageMoviesUiState() {
        viewModel.uiState.collectFlowWithDiffing(
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

    private fun manageSwitchingFavourite() {
        binding.fabFavouriteMovieDetails.setOnClickListener {
            viewModel.onFavouriteClicked()
        }
    }

    private fun displayMovieDetails(movieDetails: MovieDetails) {
        with(movieDetails) {
            displayMovieImage(this)
            displayMovieTranslatedTitle(translatedTitle)
            displayMovieOriginalTitle(originalTitle)
            displayMovieReleaseDate(releaseDate)
            displayMovieVoteAverage(voteAverage)
            displayMovieGenres(genres)
            displayMovieOverview(overview.orEmpty())
            displayFavourite(isFavourite)
        }
    }

    private fun displayMovieImage(movieDetails: MovieDetails) {
        val pathMovieImage = movieDetails.backdropImagePath ?: movieDetails.posterPath
        if (pathMovieImage != null) {
            val urlMovieImage = BuildUrlMovieBackdropUseCase(pathMovieImage, ApiUrlsManager.PosterMovieSize.WIDTH_780PX)
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

    private fun displayFavourite(isFavourite: Boolean) {
        binding.fabFavouriteMovieDetails.imageTintList = if (isFavourite) {
            ColorStateList.valueOf(requireActivity().getColor(R.color.movie_favourite_on))
        } else {
            ColorStateList.valueOf(requireActivity().getColor(R.color.movie_favourite_off))
        }
    }
}
