package com.example.tickerapp.feature_todo.domain.model

import androidx.room.Embedded
import androidx.room.Relation
import com.example.tickerapp.feature_todo.domain.model.Entities.Label
import com.example.tickerapp.feature_todo.domain.model.Entities.Todo

data class TodoWithLabel(
    @Embedded val todo: Todo,
    @Relation(
        parentColumn = "labelId",
        entityColumn = "id"
    )
    val label: Label
)