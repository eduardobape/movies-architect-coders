package com.example.mymovies.ui.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.mymovies.R
import com.example.mymovies.databinding.MovieItemBinding
import com.example.mymovies.domain.models.MovieMainDetails
import com.example.mymovies.ui.utils.loadImageFromUrl

class MoviesAdapter(val onClickItem: (Int) -> Unit) :
    ListAdapter<MovieMainDetails, MoviesAdapter.MovieViewHolder>(MovieDiff) {

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
            movieMainDetails.posterUrl?.let {
                binding.ivMoviePoster.loadImageFromUrl(it)
            }
                ?: binding.ivMoviePoster.setImageDrawable(
                    ContextCompat.getDrawable(itemView.context, R.drawable.no_movie_poster)
                )
        }

        private fun displayMovieTitle(movieMainDetails: MovieMainDetails) {
            binding.tvMovieTitle.text = movieMainDetails.translatedTitle
        }
    }

    object MovieDiff : DiffUtil.ItemCallback<MovieMainDetails>() {
        override fun areItemsTheSame(
            oldItem: MovieMainDetails,
            newItem: MovieMainDetails
        ): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: MovieMainDetails,
            newItem: MovieMainDetails
        ): Boolean =
            oldItem == newItem

    }
}