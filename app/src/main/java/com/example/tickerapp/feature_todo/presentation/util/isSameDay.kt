package com.example.tickerapp.feature_todo.presentation.util

import java.util.Calendar
import java.util.TimeZone

fun isSameDay(timestamp1: Long, timestamp2: Long): Boolean {
    val calendar1 = Calendar.getInstance()
    calendar1.timeZone = TimeZone.getDefault() // Set to user's timezone
    calendar1.timeInMillis = timestamp1

    val calendar2 = Calendar.getInstance()
    calendar2.timeZone = TimeZone.getDefault() // Set to user's timezone
    calendar2.timeInMillis = timestamp2

    return (calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
            calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR))
}