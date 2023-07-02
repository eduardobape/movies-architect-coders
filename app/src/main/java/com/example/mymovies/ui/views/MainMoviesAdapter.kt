package com.example.mymovies.ui.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.mymovies.R
import com.example.mymovies.data.remote.services.ApiUrlsManager.ApiImageUtils.PosterMovieSize
import com.example.mymovies.databinding.MovieItemBinding
import com.example.mymovies.domain.models.MovieMainDetails
import com.example.mymovies.domain.usecases.GetUrlMoviePosterUseCase
import com.example.mymovies.ui.extensions.basicDiffUtil
import com.example.mymovies.ui.extensions.loadImageFromUrl


class MainMoviesAdapter(val onClickItem: (Int) -> Unit) :
    ListAdapter<MovieMainDetails, MainMoviesAdapter.MovieViewHolder>(
        basicDiffUtil { oldMovieItem, newMovieItem -> oldMovieItem.id == newMovieItem.id }
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        return MovieViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MovieViewHolder(itemView: View) : ViewHolder(itemView) {
        private val binding: MovieItemBinding = MovieItemBinding.bind(itemView)

        fun bind(movieMainDetails: MovieMainDetails) {
            displayMovieDetails(movieMainDetails)
            itemView.setOnClickListener {
                onClickItem(movieMainDetails.id)
            }
        }

        private fun displayMovieDetails(movieMainDetails: MovieMainDetails) {
            displayMoviePoster(movieMainDetails)
            displayMovieTitle(movieMainDetails)
        }

        private fun displayMoviePoster(movieMainDetails: MovieMainDetails) {
            if (movieMainDetails.hasPoster()) {
                val urlMoviePoster = GetUrlMoviePosterUseCase(
                    movieMainDetails.posterPath!!,
                    PosterMovieSize.WIDTH_342PX
                )
                binding.ivMoviePoster.loadImageFromUrl(urlMoviePoster)
            } else {
                binding.ivMoviePoster.setImageDrawable(
                    ContextCompat.getDrawable(itemView.context, R.drawable.no_movie_poster)
                )
            }
        }

        private fun displayMovieTitle(movieMainDetails: MovieMainDetails) {
            binding.tvMovieTitle.text = movieMainDetails.translatedTitle
        }
    }
}
