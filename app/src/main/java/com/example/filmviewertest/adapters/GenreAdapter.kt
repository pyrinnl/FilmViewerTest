package com.example.filmviewertest.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.filmviewertest.databinding.GenreItemBinding

internal class GenreAdapter(private val onItemClick: (Int) -> Unit):RecyclerView.Adapter<GenreAdapter.GenreViewHolder>() {


    var genreList: List<String> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = GenreItemBinding.inflate(inflater, parent,false)
        return GenreViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        val genre = genreList[position]
        holder.binding.genreTV.text = genre
        holder.itemView.setOnClickListener{
            Log.d("TAGTAG", "$position")
            onItemClick(position)
        }
    }

    override fun getItemCount(): Int = genreList.size



    class GenreViewHolder(val binding: GenreItemBinding): RecyclerView.ViewHolder(binding.root)
}