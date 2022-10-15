package com.example.filmviewertest.screens.main

internal sealed class MainScreenAction {

    internal data class OpenFilmDetailsFragment(val id: String) : MainScreenAction()
    internal data class ShowToast(val message: String) : MainScreenAction()
}