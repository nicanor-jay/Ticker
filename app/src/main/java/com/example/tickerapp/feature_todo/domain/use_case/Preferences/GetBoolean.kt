package com.example.tickerapp.feature_todo.domain.use_case.Preferences

import com.example.tickerapp.feature_todo.domain.repository.DataStoreRepository
import com.example.tickerapp.feature_todo.domain.util.DataStoreResult

class GetBoolean(private val repository: DataStoreRepository) {
    suspend operator fun invoke(key: String): DataStoreResult<Boolean?> {
        return try {
            val result = repository.getBoolean(key)
            DataStoreResult.Success(result)
        } catch (e: Exception) {
            DataStoreResult.Error(e)
        }
    }
}