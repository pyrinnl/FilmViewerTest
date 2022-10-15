package com.example.filmviewertest.retrofit

import com.example.filmviewertest.retrofit.entities.FilmsResponseEntity
import retrofit2.http.GET

internal interface FilmsApi {
    @GET("sequeniatesttask/films.json")
   suspend fun getFilms(): FilmsResponseEntity
}