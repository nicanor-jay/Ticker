package com.example.tickerapp.feature_todo.domain.use_case.Labels

import com.example.tickerapp.feature_todo.domain.model.Entities.Label
import com.example.tickerapp.feature_todo.domain.repository.LabelRepository
import kotlinx.coroutines.flow.Flow

class GetLabels(private val repository: LabelRepository) {
    operator fun invoke(): Flow<List<Label>> {
        return repository.getLabelsFlow()
    }
}