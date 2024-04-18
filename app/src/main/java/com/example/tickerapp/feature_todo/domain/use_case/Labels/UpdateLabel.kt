package com.example.tickerapp.feature_todo.domain.use_case.Labels

import com.example.tickerapp.feature_todo.domain.model.Entities.InvalidLabelException
import com.example.tickerapp.feature_todo.domain.model.Entities.Label
import com.example.tickerapp.feature_todo.domain.repository.LabelRepository

class UpdateLabel(private val repository: LabelRepository) {
    suspend operator fun invoke(label: Label) {
        if (label.labelString.isBlank()) {
            throw InvalidLabelException("Label name cannot be empty")
        }
        repository.updateLabel(label)
    }
}