package com.example.filmviewertest.model.entities

import com.example.filmviewertest.retrofit.entities.FilmsResponseEntity

internal data class MainScreenData(
    val genres: List<String>,
    val filmList: List<FilmsResponseEntity.Film>
) {

    /**
     * Базовый класс, который может содержать в себе элементы для отрисовки нашей View на конкретном экране (Какая-то реклама,
     * список элементов из запроса или нескольких запросов и так далее)
     *
     * В данном случае нам нужно отрисовать список однородных элементов, а так же список жанров для фильтра списка.
     */
}