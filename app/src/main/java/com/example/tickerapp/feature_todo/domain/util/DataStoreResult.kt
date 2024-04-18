package com.example.tickerapp.feature_todo.domain.util

sealed class DataStoreResult<out T> {
    data class Success<T>(val data: T) : DataStoreResult<T>()
    data class Error(val exception: Exception) : DataStoreResult<Nothing>()
}
