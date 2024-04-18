package com.example.tickerapp.feature_todo.domain.util

import java.util.Calendar

fun getDaysDifference(timestamp1: Long, timestamp2: Long): Int {
    val calendar1 = Calendar.getInstance().apply { timeInMillis = timestamp1 }
    val calendar2 = Calendar.getInstance().apply { timeInMillis = timestamp2 }

    val dayOfYear1 = calendar1.get(Calendar.DAY_OF_YEAR)
    val dayOfYear2 = calendar2.get(Calendar.DAY_OF_YEAR)

    return dayOfYear2 - dayOfYear1
}