package com.example.tickerapp.feature_todo.data.repository

import com.example.tickerapp.feature_todo.data.data_source.LabelDao
import com.example.tickerapp.feature_todo.domain.model.Entities.Label
import com.example.tickerapp.feature_todo.domain.repository.LabelRepository
import kotlinx.coroutines.flow.Flow

class LabelRepositoryImpl(
    private val dao: LabelDao
) : LabelRepository {
    override fun getLabelsFlow(): Flow<List<Label>> {
        return dao.getLabelsFlow()
    }

    override suspend fun insertLabel(label: Label) {
        return dao.insertLabel(label)
    }

    override suspend fun updateLabel(label: Label) {
        return dao.updateLabel(label)
    }

    override suspend fun deleteLabel(label: Label) {
        return dao.deleteLabel(label)
    }
}