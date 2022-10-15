package com.example.filmviewertest.screens.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmviewertest.model.MainScreenRepository
import com.example.filmviewertest.retrofit.entities.FilmsResponseEntity.Film
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class MainViewModel @Inject constructor(
    private val mainScreenRepository: MainScreenRepository
) : ViewModel() {

    private val _currentGenre: MutableLiveData<String?> = MutableLiveData(null)

    private val _sourceFilmList: MutableLiveData<List<Film>> = MutableLiveData()

    private val _state = MediatorLiveData<State>().apply {
        addSource(_currentGenre) { genre ->
            if (value !is State.Loaded) return@addSource
            val currentValue = value as State.Loaded
            value = sortFilmsList(
                loadedState = currentValue.copy(filmList = _sourceFilmList.value?: emptyList()),
                genre = genre
            )
        }
        addSource(_sourceFilmList) { filmList ->
            if(value !is State.Loaded) return@addSource
            val currentValue = value as State.Loaded
            value = sortFilmsList(
                loadedState = currentValue.copy(filmList = filmList),
                genre = _currentGenre.value
            )
        }
    }
    val state: LiveData<State> = _state
    fun onGenreClicked(genre: String) {
        if (genre == _currentGenre.value) _currentGenre.value = genre
        _currentGenre.value = genre
    }


    private val _action: Channel<MainScreenAction> = Channel(Channel.BUFFERED)
    val action: Flow<MainScreenAction> = _action.receiveAsFlow()

    private var loadingMainScreenJob: Job? = null

    init {
        resubscribe()
    }

    private fun onErrorMainClick() {
        resubscribe()
    }

    private fun resubscribe() {
        _state.value = State.Loading
        loadingMainScreenJob = viewModelScope.launch(Dispatchers.IO) {
            when (val result = mainScreenRepository.getMainScreen()) {

                is MainScreenRepository.MainScreenResponse.Error -> {
                    _state.postValue(State.Error(
                        error = result.message,
                        onTryResubscribe = { onErrorMainClick() }
                    ))
                }
                is MainScreenRepository.MainScreenResponse.Success -> {
                    _sourceFilmList.postValue(result.mainScreenData.filmList)
                    _state.postValue(
                        State.Loaded(
                            filmList = result.mainScreenData.filmList,
                            genres = result.mainScreenData.genres
                        )
                    )
                }
            }
        }
    }

    private fun sortFilmsList(loadedState: State.Loaded, genre: String?): State.Loaded {
        if (genre == null && _state.value is State.Loaded) return (_state.value as State.Loaded).copy(filmList = _sourceFilmList.value?: emptyList())
        return loadedState.copy(filmList = loadedState.filmList.filter { it.genres.contains(genre) })

    }

    sealed interface State {
        object Loading : State
        data class Error(val error: String, val onTryResubscribe: () -> Unit) : State
        data class Loaded(
            val filmList: List<Film>,
            val genres: List<String>
        ) : State
    }

    override fun onCleared() {
        loadingMainScreenJob?.cancel()
        loadingMainScreenJob = null
        super.onCleared()
    }
}