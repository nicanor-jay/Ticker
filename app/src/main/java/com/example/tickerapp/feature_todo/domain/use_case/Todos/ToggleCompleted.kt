package com.example.tickerapp.feature_todo.domain.use_case.Todos

import com.example.tickerapp.feature_todo.domain.model.Entities.Todo
import com.example.tickerapp.feature_todo.domain.repository.TodoRepository

class ToggleCompleted(private val repository: TodoRepository) {

    suspend operator fun invoke(todo: Todo) {
        val todoModified: Todo
        if (todo.isCompleted) {
            // If todo is completed, update timeStampDateCompleted to current time
            todoModified = todo.copy(
//                Keep old timestamp for completed section sorting order
//                timestampCompleted = 0
            )
        } else {
            // If todo is not completed, reset timeStampDateCompleted to 0
            val completedTime = System.currentTimeMillis()
            todoModified = todo.copy(
                timestampCompleted = completedTime
            )
        }
        return repository.toggleCompleted(todoModified)
    }
}