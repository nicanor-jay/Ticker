package com.example.tickerapp.feature_todo.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.tickerapp.feature_todo.data.converters.PriorityConverter
import com.example.tickerapp.feature_todo.domain.model.Entities.Label
import com.example.tickerapp.feature_todo.domain.model.Entities.Todo

@Database(
    entities = [Todo::class, Label::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(PriorityConverter::class) // Register PriorityConverter
abstract class TodoDatabase : RoomDatabase() {

    abstract val todoDao: TodoDao

    abstract val labelDao: LabelDao

    companion object {
        const val DATABASE_NAME = "todos_db"
    }
}