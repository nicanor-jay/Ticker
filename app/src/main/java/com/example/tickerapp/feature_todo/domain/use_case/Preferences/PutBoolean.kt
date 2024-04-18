package com.example.tickerapp.feature_todo.domain.use_case.Preferences

import com.example.tickerapp.feature_todo.domain.repository.DataStoreRepository
import com.example.tickerapp.feature_todo.domain.util.DataStoreResult

class PutBoolean(private val repository: DataStoreRepository) {
    suspend operator fun invoke(key: String, value: Boolean): DataStoreResult<Unit> {
        return try {
            repository.putBoolean(key, value)
            DataStoreResult.Success(Unit)
        } catch (e: Exception) {
            DataStoreResult.Error(e)
        }
    }
}