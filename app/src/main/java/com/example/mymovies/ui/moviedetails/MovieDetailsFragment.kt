package com.example.mymovies.ui.moviedetails

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.mymovies.R
import com.example.mymovies.appContext
import com.example.mymovies.data.repositories.MoviesRepository
import com.example.mymovies.databinding.FragmentMovieDetailsBinding
import com.example.mymovies.domain.Error
import com.example.mymovies.domain.MovieGenre
import com.example.mymovies.domain.MovieImageSize
import com.example.mymovies.framework.database.MoviesRoomDataSource
import com.example.mymovies.framework.webservice.MoviesWebServiceDataSource
import com.example.mymovies.framework.webservice.RemoteConnection
import com.example.mymovies.ui.shared.collectFlowWithDiffing
import com.example.mymovies.ui.shared.launchAndCollectFlow
import com.example.mymovies.ui.shared.loadImageFromUrl
import com.example.mymovies.ui.shared.viewLifecycleBinding
import com.example.mymovies.ui.shared.visible
import com.example.mymovies.usecases.BuildMovieImageUrlUseCase
import com.example.mymovies.usecases.GetCachedMovieDetailsUseCase
import com.example.mymovies.usecases.RequestMovieDetailsUseCase
import com.example.mymovies.usecases.SwitchMovieFavouriteUseCase
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.flow.map


class MovieDetailsFragment : Fragment(R.layout.fragment_movie_details) {

    private val binding: FragmentMovieDetailsBinding by viewLifecycleBinding {
        FragmentMovieDetailsBinding.bind(requireView())
    }
    private val args: MovieDetailsFragmentArgs by navArgs()
    private val viewModel by viewModels<MovieDetailsViewModel> {
        val repository = MoviesRepository(
            MoviesWebServiceDataSource(RemoteConnection.moviesApiServices),
            MoviesRoomDataSource(requireActivity().appContext.database.movieDao())
        )
        MovieDetailsViewModel.Factory(
            getMovieIdFromSafeArgs(),
            RequestMovieDetailsUseCase(repository),
            GetCachedMovieDetailsUseCase(repository),
            BuildMovieImageUrlUseCase(repository, MovieImageSize.WIDTH_780PX),
            SwitchMovieFavouriteUseCase(repository)
        )
    }
    private lateinit var movieDetailsState: MovieDetailsState

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        movieDetailsState = buildMovieDetailsState()
        handleUiStateChanges()
        onFavouriteClicked()
    }

    private fun getMovieIdFromSafeArgs(): Long {
        return args.movieId
    }

    private fun handleUiStateChanges() {
        handleLoadingUiStateChanges()
        handleMoviesUiStateChanges()
        handleErrorUiStateChanges()
    }

    private fun handleLoadingUiStateChanges() {
        viewModel.uiState.collectFlowWithDiffing(
            viewLifecycleOwner,
            { it.isLoading },
            { binding.pbMovieDetails.visible = it }
        )
    }

    private fun handleMoviesUiStateChanges() {
        viewModel.uiState.collectFlowWithDiffing(
            viewLifecycleOwner,
            { it.movieDetails },
            {
                it?.let { movieDetails: MovieDetailsUiModel ->
                    displayMovieDetails(movieDetails)
                    binding.nesScrollMovieDetails.visible = true
                }
            }
        )
    }

    private fun handleErrorUiStateChanges() {
        viewLifecycleOwner.launchAndCollectFlow(
            viewModel.uiState.map { it.error },
            { error ->
                error?.let {
                    displayErrorMessage(it)
                    viewModel.notifyErrorShown()
                }
            }
        )
    }

    private fun displayMovieDetails(movieDetails: MovieDetailsUiModel) {
        with(movieDetails) {
            displayMovieImage(this)
            displayMovieTranslatedTitle(localTitle)
            displayMovieOriginalTitle(originalTitle)
            displayMovieReleaseDate(releaseDate)
            displayMovieVoteAverage(voteAverage)
            displayMovieGenres(genres)
            displayMovieOverview(overview.orEmpty())
            displayFavourite(isFavourite)
        }
    }

    private fun displayMovieImage(movieDetails: MovieDetailsUiModel) {
        if (movieDetails.hasBackdropImageUrl()) {
            binding.ivMovieHeaderImage.loadImageFromUrl(movieDetails.backdropImageUrl!!)
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

    private fun displayMovieGenres(movieGenres: List<MovieGenre>) {
        binding.tvMovieGenresNames.text = movieGenres.joinToString(separator = ", ") {
            it.name
        }
    }

    private fun displayMovieOverview(movieOverview: String) {
        binding.tvMovieOverview.text = movieOverview.ifEmpty {
            resources.getString(R.string.movie_overview_headline)
        }
    }

    private fun displayErrorMessage(error: Error) {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(movieDetailsState.getErrorMessage(error))
            .setPositiveButton(getString(R.string.default_positive_button_dialog), null)
            .show()
    }

    private fun displayFavourite(isFavourite: Boolean) {
        binding.fabFavouriteMovieDetails.imageTintList = if (isFavourite) {
            ColorStateList.valueOf(requireActivity().getColor(R.color.movie_favourite_on))
        } else {
            ColorStateList.valueOf(requireActivity().getColor(R.color.movie_favourite_off))
        }
    }

    private fun onFavouriteClicked() {
        binding.fabFavouriteMovieDetails.setOnClickListener {
            viewModel.onFavouriteClicked()
        }
    }
}
