package com.example.mymovies.ui.paginatedmovies

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.mymovies.R
import com.example.mymovies.databinding.PaginatedMovieItemBinding
import com.example.mymovies.ui.shared.basicDiffUtilForAdapter
import com.example.mymovies.ui.shared.loadImageFromUrl


class PaginatedMoviesMainAdapter(private val onClickItem: (Long) -> Unit) :
    ListAdapter<PaginatedMovieUiModel, PaginatedMoviesMainAdapter.MovieViewHolder>(
        basicDiffUtilForAdapter { oldMovieItem, newMovieItem -> oldMovieItem.id == newMovieItem.id }
    ) {

    var onClickMovieActivated: Boolean = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.paginated_movie_item, parent, false)
        return MovieViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun changeActivationOnClickMovie(activated: Boolean) {
        onClickMovieActivated = activated
    }

    inner class MovieViewHolder(itemView: View) : ViewHolder(itemView) {
        private val binding: PaginatedMovieItemBinding = PaginatedMovieItemBinding.bind(itemView)

        fun bind(movie: PaginatedMovieUiModel) {
            displayMovieDetails(movie)
            itemView.setOnClickListener {
                if (onClickMovieActivated) {
                    onClickItem(movie.id)
                }
            }
        }

        private fun displayMovieDetails(movie: PaginatedMovieUiModel) {
            displayMoviePoster(movie)
            displayMovieTitle(movie)
            displayFavourite(movie)
        }

        private fun displayMoviePoster(movie: PaginatedMovieUiModel) {
            if (movie.hasPoster()) {
                binding.ivMoviePoster.loadImageFromUrl(movie.posterUrl!!)
            } else {
                binding.ivMoviePoster.setImageDrawable(
                    ContextCompat.getDrawable(itemView.context, R.drawable.no_movie_poster)
                )
            }
        }

        private fun displayMovieTitle(movie: PaginatedMovieUiModel) {
            binding.tvMovieTitle.text = movie.localTitle
        }

        private fun displayFavourite(movie: PaginatedMovieUiModel) {
            binding.ivFavouriteMoviePaginated.imageTintList = if (movie.isFavourite) {
                ColorStateList.valueOf(itemView.context.getColor(R.color.movie_favourite_on))
            } else {
                ColorStateList.valueOf(itemView.context.getColor(R.color.movie_favourite_off))
            }
        }
    }
}
