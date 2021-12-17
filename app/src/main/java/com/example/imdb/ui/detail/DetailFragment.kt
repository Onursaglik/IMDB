package com.example.imdb.ui.detail

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.imdb.R
import com.example.imdb.model.Movie


class DetailFragment : Fragment(R.layout.fragment_detail) {

    lateinit var movieName: TextView
    lateinit var movieDetail: TextView
    lateinit var movieImage: ImageView
    lateinit var movieVote: TextView
    lateinit var movieDate: TextView
    lateinit var favourite: CheckBox
    var stringHashSet: Set<String>? = null
    var movieId: Int? = null
    var movie: Movie? = null
    private val detailViewModel: DetailViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val preferences = context?.getSharedPreferences("preferences", Context.MODE_PRIVATE)
        stringHashSet = preferences?.getStringSet("favorites", HashSet<String>())
        activity?.actionBar?.hide()

        movieName = view.findViewById(R.id.MovieName)
        movieDetail = view.findViewById(R.id.MovieDetail)
        movieImage = view.findViewById(R.id.imageView2)
        movieDate = view.findViewById(R.id.releaseDate)
        movieVote = view.findViewById(R.id.voteScore)


        if (arguments?.getBoolean("isFavoriteFragment", false) == false) {
            detailViewModel.getMovieDetail(arguments?.getInt("id")!!)
            observe()
        } else if (arguments?.getBoolean("isFavoriteFragment", false) == true) {

            var moviex = arguments?.getSerializable("movie") as Movie
            moviex.let {
                movie = it
                movieId = it.id
                movieDetail.text = it.overview
                favourite.isChecked = true
                movieName.text = it.original_title
                movieDate.append(it.release_date)
                movieVote.append(it.vote_average.toString())
                Glide.with(movieImage.context).load("https://image.tmdb.org/t/p/w500" + it.backdrop_path)

                        .apply(RequestOptions().placeholder(R.drawable.nophoto).error(R.drawable.nophoto))
                        .into(movieImage)
            }
        }
    }

    fun observe() {

        detailViewModel.showLiveData.observe(requireActivity()) {
            movie = it
            movieId = it.id
            movieDetail.text = it.overview

            movieName.text = it.original_title
            movieDate.append(it.release_date)
            movieVote.append(it.vote_average.toString())
            Glide.with(movieImage.context).load("https://image.tmdb.org/t/p/w500" + it.backdrop_path)
                    .apply(RequestOptions().placeholder(R.drawable.nophoto).error(R.drawable.nophoto))
                    .into(movieImage)

            if (stringHashSet!!.contains(movieId.toString())) {
                favourite.isChecked = true

            }
        }
        detailViewModel.showError.observe(requireActivity()) {

            if (it == true) {
                if (context != null)
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
            }
        }
    }
}