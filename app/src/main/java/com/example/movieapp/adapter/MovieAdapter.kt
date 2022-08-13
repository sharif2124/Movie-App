package com.example.movieapp.adapter

import android.content.Context
import android.graphics.Matrix
import android.support.v4.os.IResultReceiver
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import com.example.movieapp.R
import com.example.movieapp.databinding.ItemMoviesBinding
import com.example.movieapp.response.MoviesListResponse
import com.example.movieapp.utils.Constants.POSTER_BASE_URL

import kotlinx.coroutines.NonDisposableHandle.parent

class MovieAdapter:RecyclerView.Adapter<MovieAdapter.Viewholder>() {
    private lateinit var binding: ItemMoviesBinding
    private lateinit var context: Context
    inner class Viewholder:RecyclerView.ViewHolder(binding.root){
       fun bind(item:MoviesListResponse.Result)
       {
           binding.apply {
            tvMovieName.text=item.title
               tvRate.text=item.voteAverage.toString()
               val moviePosterURL =POSTER_BASE_URL + item.posterPath
               imgMove.load(moviePosterURL){
                crossfade(true)
                   placeholder(R.drawable.poster_placeholder)
                   scale(Scale.FILL)
               }
               tvLang.text=item.originalLanguage
               tvDateRelease.text=item.releaseDate
           }

       }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
       val inflater=LayoutInflater.from(parent.context)
        binding=ItemMoviesBinding.inflate(inflater,parent,false)
        context=parent.context
        return Viewholder()
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private val differCallback= object :DiffUtil.ItemCallback<MoviesListResponse.Result>(){
        override fun areItemsTheSame(
            oldItem: MoviesListResponse.Result,
            newItem: MoviesListResponse.Result
        ): Boolean {
            return oldItem.id==newItem.id
        }

        override fun areContentsTheSame(
            oldItem: MoviesListResponse.Result,
            newItem: MoviesListResponse.Result
        ): Boolean {
            return oldItem==newItem
        }

    }
    val differ = AsyncListDiffer(this,differCallback)

}