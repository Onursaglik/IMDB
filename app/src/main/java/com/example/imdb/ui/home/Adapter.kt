package com.example.imdb.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.imdb.R
import com.example.imdb.model.Movie


class Adapter : RecyclerView.Adapter<Adapter.MyViewHolder>(){
    var isLinearLayout = false
    var movies = ArrayList<Movie>()
    var recyclerViewClickInterface: RecyclerViewClickInterface? = null
    var context: Context? = null


    override fun getItemViewType(position: Int): Int {
        if (isLinearLayout)
            return 1;
        else
            return 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val context = parent.context
        val view: View
        if (viewType == 0)
            view = LayoutInflater.from(context).inflate(R.layout.movie_item, parent, false)
        else
            view = LayoutInflater.from(context).inflate(R.layout.movie_item, parent, false)
        return MyViewHolder(view)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(movies.get(position))
        holder.itemView.setOnClickListener {
            recyclerViewClickInterface?.onItemClick(movies.get(position));
        }
    }


    fun addData(listItems: ArrayList<Movie>) {
        var size = this.movies.size
        this.movies.addAll(listItems)
        var sizeNew = this.movies.size
        notifyItemRangeChanged(size, sizeNew)
        notifyDataSetChanged()
    }


    override fun getItemCount() = movies?.size ?: 0


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        private val imageView: ImageView = itemView.findViewById(R.id.imageView)
        private val textView: TextView = itemView.findViewById(R.id.textView)
        private val textView2: TextView = itemView.findViewById(R.id.textView2)
        private val textView4: TextView = itemView.findViewById(R.id.textView4)

        fun bind(movie: Movie) {
            textView.text = movie.original_title
            textView2.text = movie.release_date
            textView4.text = movie.vote_average.toString()
            Glide.with(imageView.context).load("https://image.tmdb.org/t/p/w500" + movie.backdrop_path)
                    .apply( RequestOptions().placeholder(R.drawable.nophoto).error(R.drawable.nophoto))
                    .into(imageView)
        }
    }
}