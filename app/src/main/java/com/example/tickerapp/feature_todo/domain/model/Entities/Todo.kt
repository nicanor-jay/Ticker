package com.example.tickerapp.feature_todo.domain.model.Entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.tickerapp.feature_todo.domain.model.Priority

@Entity(
    tableName = "todos",
    foreignKeys = [
        ForeignKey(
            entity = Label::class,
            parentColumns = ["id"],
            childColumns = ["labelId"],
            onDelete = ForeignKey.SET_DEFAULT // or ForeignKey.CASCADE, depending on your requirements
        )
    ]
)
data class Todo(
    @PrimaryKey val id: Int? = null,
    val title: String,
    val priority: Priority,
    @ColumnInfo(defaultValue = "1")
    val labelId: Int, // Use the foreign key column instead of label string
    val isCompleted: Boolean,
    val timestampCreated: Long = System.currentTimeMillis(),
    val timeStampDueDate: Long = 0,
    val timestampCompleted: Long = 0
) {
    companion object {
        val todoPriority = listOf(Priority.LOW, Priority.MEDIUM, Priority.HIGH) // Use enum values
    }
}

class InvalidTodoException(message: String) : Exception(message)