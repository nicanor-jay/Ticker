package com.example.tickerapp.feature_todo.domain.use_case.Labels

import com.example.tickerapp.feature_todo.domain.model.Entities.Label
import com.example.tickerapp.feature_todo.domain.repository.LabelRepository

class DeleteLabel(private val repository: LabelRepository) {

    suspend operator fun invoke(label: Label) {
        repository.deleteLabel(label)
    }
}