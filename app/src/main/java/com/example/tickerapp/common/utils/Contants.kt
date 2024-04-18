package com.example.tickerapp.common.utils

import com.example.tickerapp.feature_todo.domain.model.Entities.Label

object Constants {
    const val PREFERENCES_NAME = "user_preferences"

    const val ORDER_TYPE_KEY = "order_type_key"
    const val ORDER_DIRECTION_KEY = "order_direction_key"
    const val LABEL_CHARACTER_LIMIT = 15
    const val TODO_CHARACTER_LIMIT = 75

    val DEFAULT_LABEL = Label(1, "None (default)")
}