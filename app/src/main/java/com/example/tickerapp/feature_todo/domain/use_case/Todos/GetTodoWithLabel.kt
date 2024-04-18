package com.example.tickerapp.feature_todo.domain.use_case.Todos

import com.example.tickerapp.feature_todo.domain.model.TodoWithLabel
import com.example.tickerapp.feature_todo.domain.repository.TodoRepository

class GetTodoWithLabel(private val repository: TodoRepository) {
    suspend operator fun invoke(id: Int): TodoWithLabel? {
        return repository.getTodoWithLabelById(id)
    }
}