package com.example.tickerapp.feature_todo.presentation.util

import com.example.tickerapp.feature_todo.domain.util.getDaysDifference

fun isAfterDay(timestamp1: Long, timestamp2: Long): Boolean {
    return getDaysDifference(timestamp1, timestamp2) < 0
}