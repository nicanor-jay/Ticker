package com.example.tickerapp.feature_todo.domain.model.Entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "labels",
    indices = [Index(value = ["labelString"], unique = true)]
)
data class Label(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val labelString: String
)

class InvalidLabelException(message: String) : Exception(message)