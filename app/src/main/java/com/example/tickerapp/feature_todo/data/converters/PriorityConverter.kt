package com.example.tickerapp.feature_todo.data.converters

import androidx.room.TypeConverter
import com.example.tickerapp.feature_todo.domain.model.Priority

class PriorityConverter {
    @TypeConverter
    fun fromPriority(priority: Priority): String {
        return priority.name // Convert enum to string
    }

    @TypeConverter
    fun toPriority(priority: String): Priority {
        return enumValueOf(priority) // Convert string to enum
    }
}