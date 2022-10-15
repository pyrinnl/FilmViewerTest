package com.example.filmviewertest.screens.main

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.filmviewertest.R
import com.example.filmviewertest.adapters.FilmsAdapter
import com.example.filmviewertest.adapters.GenreAdapter
import com.example.filmviewertest.databinding.FragmentMainBinding
import com.example.filmviewertest.screens.main.MainViewModel.State
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class MainFragment : Fragment(R.layout.fragment_main) {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: FragmentMainBinding
    private lateinit var filmsAdapter: FilmsAdapter
    private lateinit var genresAdapter: GenreAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainBinding.bind(view)

        viewModel.state.observe(viewLifecycleOwner) { state ->
            prepareFilmAdapter(state)
            prepareGenreListAdapter(state)
            renderUI(state)
        }
    }

    private fun prepareGenreListAdapter(state: State) {
        if (state !is State.Loaded) return
        val genres: List<String> = state.genres
        genresAdapter = GenreAdapter { onGenreClick(genres[it]) }
        genresAdapter.genreList = genres
        binding.genreRecyclerView.adapter = genresAdapter
    }

    private fun prepareFilmAdapter(state: State) {
        if (state !is State.Loaded) return
        val films = state.filmList
        filmsAdapter = FilmsAdapter()
        filmsAdapter.filmListData = films
        binding.filmsRecyclerView.adapter = filmsAdapter
    }

    private fun onGenreClick(genre: String) {
        viewModel.onGenreClicked(genre)
    }

    private fun renderUI(state: State) {
        binding.progressBar.visibility = if (state is State.Loading) View.VISIBLE else View.GONE
        binding.coordinatorLayout.visibility =
            if (state is State.Loaded) View.VISIBLE else View.GONE

        binding.errorItem.layout.visibility = if (state is State.Error) {
            binding.errorItem.errorTitle.text = state.error
            binding.errorItem.button.setOnClickListener {
                state.onTryResubscribe()
            }
            View.VISIBLE
        } else {
            View.GONE
        }
    }
}