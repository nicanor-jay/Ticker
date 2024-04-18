package com.example.tickerapp.feature_todo.domain.use_case.Preferences

import com.example.tickerapp.feature_todo.domain.repository.DataStoreRepository
import com.example.tickerapp.feature_todo.domain.util.DataStoreResult

class PutString(private val repository: DataStoreRepository) {
    suspend operator fun invoke(key: String, value: String): DataStoreResult<Unit> {
        return try {
            repository.putString(key, value)
            DataStoreResult.Success(Unit)
        } catch (e: Exception) {
            DataStoreResult.Error(e)
        }
    }
}