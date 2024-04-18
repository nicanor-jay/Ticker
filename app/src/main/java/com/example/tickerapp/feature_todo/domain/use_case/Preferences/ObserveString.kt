package com.example.tickerapp.feature_todo.domain.use_case.Preferences

import com.example.tickerapp.feature_todo.domain.repository.DataStoreRepository
import kotlinx.coroutines.flow.Flow

class ObserveString(private val repository: DataStoreRepository) {
    operator fun invoke(key: String, defaultValue: String): Flow<String> {
        return repository.observeString(key, defaultValue)
    }
}