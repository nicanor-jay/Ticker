package com.example.tickerapp.feature_todo.domain.repository

import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {
    fun observeBoolean(key: String): Flow<Boolean>
    fun observeString(key: String, defaultValue: String): Flow<String>

    suspend fun putString(key: String, value: String)
    suspend fun putBoolean(key: String, value: Boolean)
    suspend fun getString(key: String): String?
    suspend fun getBoolean(key: String): Boolean?
}