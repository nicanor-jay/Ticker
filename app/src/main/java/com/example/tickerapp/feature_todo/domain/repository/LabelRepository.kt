package com.example.tickerapp.feature_todo.domain.repository

import com.example.tickerapp.feature_todo.domain.model.Entities.Label
import kotlinx.coroutines.flow.Flow

interface LabelRepository {
    fun getLabelsFlow(): Flow<List<Label>>

    suspend fun insertLabel(label: Label)
    suspend fun updateLabel(label: Label)
    suspend fun deleteLabel(label: Label)

}