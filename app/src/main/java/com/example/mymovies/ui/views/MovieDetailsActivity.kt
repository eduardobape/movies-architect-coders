package com.example.mymovies.ui.views

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.mymovies.R
import com.example.mymovies.data.remote.client.RetrofitServiceBuilder
import com.example.mymovies.data.remote.services.ApiUrlsManager.ApiImageUtils.PosterMovieSize
import com.example.mymovies.data.remote.services.MoviesApi
import com.example.mymovies.data.repository.MovieDetailsRepositoryImpl
import com.example.mymovies.databinding.ActivityMovieDetailBinding
import com.example.mymovies.domain.models.MovieDetails
import com.example.mymovies.domain.usecases.GetUrlMovieBackdropUseCase
import com.example.mymovies.domain.usecases.MovieDetailsUseCase
import com.example.mymovies.ui.utils.loadImageFromUrl
import com.example.mymovies.ui.utils.visible
import com.example.mymovies.ui.viewmodels.MovieDetailsViewModel
import com.google.android.material.appbar.AppBarLayout
import kotlinx.coroutines.launch
import kotlin.math.abs


class MovieDetailsActivity : AppCompatActivity() {

    companion object {
        const val MOVIE_ID = "movieID"
    }

    private lateinit var binding: ActivityMovieDetailBinding
    private val viewModel by viewModels<MovieDetailsViewModel> {
        MovieDetailsViewModel.Factory(
            MovieDetailsUseCase(
                MovieDetailsRepositoryImpl(MoviesApi(RetrofitServiceBuilder).movieDetailsApiService)
            ),
            getMovieIdFromIntent()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpActionBar()
        manageScrollCollapsingToolbar()
        hookToUiState()
    }

    private fun setUpActionBar() {
        binding.toolbarMovieDetails.apply {
            setNavigationOnClickListener {
                finish()
            }
        }
    }

    private fun manageScrollCollapsingToolbar() {
        binding.appBarMovieDetails.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            manageToolbarTitleVisibility(appBarLayout, verticalOffset)
        }
    }

    private fun manageToolbarTitleVisibility(appBarLayout: AppBarLayout, verticalOffsetAppBarLayout: Int) {
        if (abs(verticalOffsetAppBarLayout) == appBarLayout.totalScrollRange) {
            // Show the toolbar's title when the collapsing toolbar layout is fully collapsed
            modifyToolBarTitle(viewModel.getMovieTitle())
        } else if (verticalOffsetAppBarLayout == 0) {
            // Hide the toolbar's title when the collapsing toolbar layout is expanded in
            // order to not overlapping the movie's image displayed on this one
            modifyToolBarTitle("")
        }
    }

    private fun getMovieIdFromIntent(): Int? = intent.extras?.getInt(MOVIE_ID)

    private fun hookToUiState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect(::updateViewsFromUiState)
            }
        }
    }

    private fun updateViewsFromUiState(uiState: MovieDetailsState) {
        when (uiState) {
            is MovieDetailsState.Loading -> binding.pbMovieDetails.visible = true

            is MovieDetailsState.Success -> displayMovieDetails(uiState.movieDetails)

            is MovieDetailsState.Error ->
                Toast.makeText(this, getString(R.string.movie_id_intent_error), Toast.LENGTH_SHORT).show()
        }
    }

    private fun displayMovieDetails(movieDetails: MovieDetails?) {
        movieDetails?.let {
            displayMovieImage(it)
            displayMovieTranslatedTitle(it.translatedTitle)
            displayMovieOriginalTitle(it.originalTitle)
            displayMovieReleaseDate(it.releaseDate)
            displayMovieVoteAverage(it.voteAverage)
            displayMovieGenres(it.genres)
            displayMovieOverview(it.overview.orEmpty())
            binding.nesScrollMovieDetails.visible = true
        }
        binding.pbMovieDetails.visible = false
    }

    private fun modifyToolBarTitle(title: String) {
        binding.toolbarMovieDetails.title = title
    }

    private fun displayMovieImage(movieDetails: MovieDetails) {
        val pathMovieImage = movieDetails.backdropImagePath ?: movieDetails.posterPath
        if (pathMovieImage != null) {
            val urlMovieImage = GetUrlMovieBackdropUseCase(pathMovieImage, PosterMovieSize.WIDTH_780PX)
            binding.ivMovieHeaderImage.loadImageFromUrl(urlMovieImage)
        } else {
            binding.ivMovieHeaderImage.setImageDrawable(
                ContextCompat.getDrawable(this, R.drawable.no_movie_poster)
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
