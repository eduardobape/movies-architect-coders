package com.example.mymovies.ui.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.mymovies.R
import com.example.mymovies.databinding.MovieItemBinding
import com.example.mymovies.domain.models.MovieMainDetails

class MoviesAdapter : ListAdapter<MovieMainDetails, MoviesAdapter.MovieViewHolder>(MovieDiff) {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
		val itemView = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
		return MovieViewHolder(itemView)
	}

	override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
		holder.bind(getItem(position))
	}

	class MovieViewHolder(itemView: View) : ViewHolder(itemView) {
		private val binding: MovieItemBinding = MovieItemBinding.bind(itemView)

		fun bind(movieMainDetails: MovieMainDetails) {
			displayMoviePoster(movieMainDetails)
			binding.tvMovieTitle.text = movieMainDetails.translatedTitle
		}

		private fun displayMoviePoster(movieMainDetails: MovieMainDetails) {
			movieMainDetails.posterUrl?.let { Glide.with(itemView).load(it).into(binding.ivMoviePoster) }
				?: binding.ivMoviePoster.setImageDrawable(
					ContextCompat.getDrawable(itemView.context, R.drawable.no_movie_poster)
				)
		}
	}

	object MovieDiff : DiffUtil.ItemCallback<MovieMainDetails>() {
		override fun areItemsTheSame(oldItem: MovieMainDetails, newItem: MovieMainDetails): Boolean =
			oldItem.id == newItem.id

		override fun areContentsTheSame(oldItem: MovieMainDetails, newItem: MovieMainDetails): Boolean =
			oldItem == newItem

	}
}