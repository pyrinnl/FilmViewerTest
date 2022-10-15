package com.example.filmviewertest.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.filmviewertest.R
import com.example.filmviewertest.databinding.FilmItemBinding
import com.example.filmviewertest.retrofit.entities.FilmsResponseEntity

internal class FilmsAdapter :
    RecyclerView.Adapter<FilmsAdapter.FilmViewHolder>() {

    private lateinit var context: Context

    var filmListData: List<FilmsResponseEntity.Film> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FilmItemBinding.inflate(inflater, parent, false)
        context = parent.context
        return FilmViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FilmViewHolder, position: Int) {
        val film = filmListData[position]
        with(holder.binding) {
            filmLabelImage.text = film.name

            Glide
                .with(root)
                .load(film.url)
                .placeholder(R.drawable.ic_placeholder)
                .centerCrop()
                .into(filmImage)
        }
    }

    override fun getItemCount(): Int = filmListData.size

    class FilmViewHolder(val binding: FilmItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}