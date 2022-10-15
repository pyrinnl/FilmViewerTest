package com.example.filmviewertest.retrofit.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class FilmsResponseEntity(
    val films: List<Film>
) {
    @Serializable
    data class Film(
        val id: Int,
        val localized_name: String,
        val name: String,
        val year: Int,
        val rating: Double? = 0.0,
        @SerialName("image_url") val url: String? = "",
        val description: String? = "Описание отсутствует",
        val genres: List<String>
    )

    /**
     *  Здесь мы можем промапать исходные сущности под нужды наших View, но конкретно в этом случае нужды в этом я не вижу.
     */
}

