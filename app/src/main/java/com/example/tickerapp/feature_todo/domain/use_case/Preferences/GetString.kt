package com.example.tickerapp.feature_todo.domain.use_case.Preferences

import com.example.tickerapp.feature_todo.domain.repository.DataStoreRepository
import com.example.tickerapp.feature_todo.domain.util.DataStoreResult

class GetString(private val repository: DataStoreRepository) {
    suspend operator fun invoke(key: String): DataStoreResult<String?> {
        return try {
            val result = repository.getString(key)
            DataStoreResult.Success(result)
        } catch (e: Exception) {
            DataStoreResult.Error(e)
        }
    }
}