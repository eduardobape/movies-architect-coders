package com.example.mymovies.ui.views

import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.mymovies.R
import com.example.mymovies.data.remote.services.ApiUrlsManager.ApiImageUtils.PosterMovieSize
import com.example.mymovies.data.remote.client.RetrofitServiceBuilder
import com.example.mymovies.data.remote.services.MoviesApi
import com.example.mymovies.data.repository.MovieDetailsRepositoryImpl
import com.example.mymovies.databinding.ActivityMovieDetailBinding
import com.example.mymovies.domain.models.MovieDetails
import com.example.mymovies.domain.usecases.GetUrlMovieBackdropUseCase
import com.example.mymovies.domain.usecases.MovieDetailsUseCase
import com.example.mymovies.ui.utils.loadImageFromUrl
import com.example.mymovies.ui.viewmodels.MovieDetailsViewModel
import com.google.android.material.appbar.AppBarLayout
import kotlin.math.abs


class MovieDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMovieDetailBinding
    private val viewModel by viewModels<MovieDetailsViewModel> {
        MovieDetailsViewModel.Factory(
            MovieDetailsUseCase(MovieDetailsRepositoryImpl(
                MoviesApi(RetrofitServiceBuilder).movieDetailsApiService)),
            getMovieIdFromIntent()
        )
    }

    companion object {
        const val MOVIE_ID = "movieID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpActionBar()
        manageScrollCollapsingToolbar()
        loadMovieDetails()
    }

    private fun setUpActionBar() {
        setSupportActionBar(binding.toolbarMovieDetails)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun manageScrollCollapsingToolbar() {
        binding.appBarMovieDetails.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            manageToolbarTitleVisibility(appBarLayout, verticalOffset)
        }
    }

    private fun manageToolbarTitleVisibility(
        appBarLayout: AppBarLayout,
        verticalOffsetAppBarLayout: Int
    ) {
        if (abs(verticalOffsetAppBarLayout) == appBarLayout.totalScrollRange) {
            // Show the toolbar's title when the collapsing toolbar layout is fully collapsed
            supportActionBar?.setDisplayShowTitleEnabled(true)
        } else if (verticalOffsetAppBarLayout == 0) {
            // Hide the toolbar's title when the collapsing toolbar layout is collapsed in
            // order to not overlapping the movie's image displayed on this one
            supportActionBar?.setDisplayShowTitleEnabled(false)
        }
    }

    private fun getMovieIdFromIntent(): Int? {
        return intent.extras?.getInt(MOVIE_ID)
    }

    private fun loadMovieDetails() {
        viewModel.uiState.observe(this) { uiState ->
            when (uiState.loadState) {
                MovieDetailsLoadState.Loading ->
                    Toast.makeText(this, "Cargando", Toast.LENGTH_SHORT).show()

                MovieDetailsLoadState.Success -> displayMovieDetails(uiState.movieDetails)

                is MovieDetailsLoadState.Error ->
                    Toast.makeText(this, uiState.loadState.errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun displayMovieDetails(movieDetails: MovieDetails?) {
        movieDetails?.let {
            with(binding) {
                modifyToolBarTitle(it.translatedTitle)
                displayMovieImage(it, ivMovieHeaderImage)
                displayMovieTranslatedTitle(it.translatedTitle, tvMovieTranslatedTitle)
                displayMovieOriginalTitle(it.originalTitle, tvMovieOriginalTitle)
                displayMovieReleaseDate(it.releaseDate, tvMovieReleaseDate)
                displayMovieVoteAverage(it.voteAverage, tvMovieVoteAverage)
                displayMovieGenres(it.genres, tvMovieGenresNames)
                displayMovieOverview(it.overview.orEmpty(), tvMovieOverview)
            }
        }
    }

    private fun displayMovieImage(
        movieDetails: MovieDetails,
        imageViewMovieImage: ImageView
    ) {
        val pathMovieImage = movieDetails.backdropImagePath ?: movieDetails.posterPath
        if (pathMovieImage != null) {
            val urlMovieImage = GetUrlMovieBackdropUseCase(pathMovieImage, PosterMovieSize.WIDTH_780PX)
            imageViewMovieImage.loadImageFromUrl(urlMovieImage)
        } else {
            imageViewMovieImage.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.no_movie_poster
                )
            )
        }
    }

    private fun displayMovieTranslatedTitle(title: String, textViewTranslatedTitle: TextView) {
        textViewTranslatedTitle.text = title
    }

    private fun modifyToolBarTitle(title: String) {
        supportActionBar?.title = title
    }

    private fun displayMovieOriginalTitle(title: String, textViewOriginalTitle: TextView) {
        textViewOriginalTitle.text = title
    }

    private fun displayMovieReleaseDate(releaseDate: String, textViewReleaseDate: TextView) {
        textViewReleaseDate.text = releaseDate
    }

    private fun displayMovieVoteAverage(voteAverage: Float, textViewVoteAverage: TextView) {
        textViewVoteAverage.text = voteAverage.toString()
    }

    private fun displayMovieGenres(movieGenresNames: List<String>, textViewGenresNames: TextView) {
        textViewGenresNames.text = movieGenresNames.joinToString(separator = ", ")
    }

    private fun displayMovieOverview(movieOverview: String, textViewOverview: TextView) {
        textViewOverview.text = movieOverview.ifEmpty {
            resources.getString(R.string.movie_overview_headline)
        }
    }
}
