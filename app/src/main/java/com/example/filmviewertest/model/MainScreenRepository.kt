package com.example.filmviewertest.model

import com.example.filmviewertest.model.entities.MainScreenData
import com.example.filmviewertest.retrofit.FilmsApi
import com.example.filmviewertest.utills.ThisApplicationNetworkErrors.unknownError
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Репозиторий для нашего главного экрана.
 */

@Singleton
internal class MainScreenRepository @Inject constructor(
    private val filmsApi: FilmsApi
) {

    suspend fun getMainScreen(): MainScreenResponse {
        return try {
            val mainScreenResponse = filmsApi.getFilms()
            MainScreenResponse.Success(
                MainScreenData(
                    filmList = mainScreenResponse.films,
                    genres = mainScreenResponse.films.flatMap {
                        it.genres
                    }.distinct().sorted()
                )
            )
        } catch (e: Throwable) {
            MainScreenResponse.Error(e.localizedMessage ?: unknownError)
        }
    }

    sealed class MainScreenResponse {
        data class Error(val message: String) : MainScreenResponse()
        data class Success(val mainScreenData: MainScreenData) : MainScreenResponse()
    }
}