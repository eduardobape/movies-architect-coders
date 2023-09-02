package com.example.mymovies.ui.views

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.mymovies.R
import com.example.mymovies.data.remote.apiurls.ApiUrlsManager
import com.example.mymovies.databinding.PaginatedMovieItemBinding
import com.example.mymovies.domain.models.PaginatedMovieDetails
import com.example.mymovies.domain.models.hasPoster
import com.example.mymovies.domain.usecases.BuildUrlMoviePosterUseCase
import com.example.mymovies.ui.extensions.basicDiffUtilForAdapter
import com.example.mymovies.ui.extensions.loadImageFromUrl


class PaginatedMoviesMainAdapter(val onClickItem: (Long) -> Unit) :
    ListAdapter<PaginatedMovieDetails, PaginatedMoviesMainAdapter.MovieViewHolder>(
        basicDiffUtilForAdapter { oldMovieItem, newMovieItem -> oldMovieItem.id == newMovieItem.id }
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.paginated_movie_item, parent, false)
        return MovieViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MovieViewHolder(itemView: View) : ViewHolder(itemView) {
        private val binding: PaginatedMovieItemBinding = PaginatedMovieItemBinding.bind(itemView)

        fun bind(movieMainDetails: PaginatedMovieDetails) {
            displayMovieDetails(movieMainDetails)
            itemView.setOnClickListener {
                onClickItem(movieMainDetails.id)
            }
        }

        private fun displayMovieDetails(movieMainDetails: PaginatedMovieDetails) {
            displayMoviePoster(movieMainDetails)
            displayMovieTitle(movieMainDetails)
            displayFavourite(movieMainDetails)
        }

        private fun displayMoviePoster(movieMainDetails: PaginatedMovieDetails) {
            if (movieMainDetails.hasPoster()) {
                val urlMoviePoster = BuildUrlMoviePosterUseCase(
                    movieMainDetails.posterPath!!,
                    ApiUrlsManager.PosterMovieSize.WIDTH_342PX
                )
                binding.ivMoviePoster.loadImageFromUrl(urlMoviePoster)
            } else {
                binding.ivMoviePoster.setImageDrawable(
                    ContextCompat.getDrawable(itemView.context, R.drawable.no_movie_poster)
                )
            }
        }

        private fun displayMovieTitle(movieMainDetails: PaginatedMovieDetails) {
            binding.tvMovieTitle.text = movieMainDetails.translatedTitle
        }

        private fun displayFavourite(movieMainDetails: PaginatedMovieDetails) {
            binding.ivFavouriteMoviePaginated.imageTintList = if (movieMainDetails.isFavourite) {
                ColorStateList.valueOf(itemView.context.getColor(R.color.movie_favourite_on))
            } else {
                ColorStateList.valueOf(itemView.context.getColor(R.color.movie_favourite_off))
            }
        }
    }
}
