package com.example.tickerapp.feature_todo.domain.util

import java.text.SimpleDateFormat
import java.util.Locale

fun formatDate(timeStamp: Long, includeYear: Boolean = true): String {
    val pattern = if (includeYear) "dd MMMM, yyyy" else "dd MMM"
    val formatter = SimpleDateFormat(pattern, Locale.getDefault())
    return formatter.format(timeStamp)
}