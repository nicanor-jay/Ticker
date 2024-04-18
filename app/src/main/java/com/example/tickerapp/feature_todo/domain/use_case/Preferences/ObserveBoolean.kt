package com.example.tickerapp.feature_todo.domain.use_case.Preferences

import com.example.tickerapp.feature_todo.domain.repository.DataStoreRepository
import kotlinx.coroutines.flow.Flow

class ObserveBoolean(private val repository: DataStoreRepository) {
    operator fun invoke(key: String): Flow<Boolean> {
        return repository.observeBoolean(key)
    }
}