package com.example.tickerapp.feature_todo.presentation.util

import java.util.Calendar

fun isAfterDay(timestamp1: Long, timestamp2: Long): Boolean {
    val calendar1 = Calendar.getInstance().apply { timeInMillis = timestamp1 }
    val calendar2 = Calendar.getInstance().apply { timeInMillis = timestamp2 }

    // Set the time to midnight (00:00:00) to compare only the date part
    calendar1.set(Calendar.HOUR_OF_DAY, 0)
    calendar1.set(Calendar.MINUTE, 0)
    calendar1.set(Calendar.SECOND, 0)
    calendar1.set(Calendar.MILLISECOND, 0)

    calendar2.set(Calendar.HOUR_OF_DAY, 0)
    calendar2.set(Calendar.MINUTE, 0)
    calendar2.set(Calendar.SECOND, 0)
    calendar2.set(Calendar.MILLISECOND, 0)

    // Return true if the first date is before the second
    return calendar1.after(calendar2)
}