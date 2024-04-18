package com.example.tickerapp.feature_todo.domain.use_case.Todos

import com.example.tickerapp.feature_todo.domain.model.Entities.Todo
import com.example.tickerapp.feature_todo.domain.repository.TodoRepository


class GetTodo(private val repository: TodoRepository) {

    suspend operator fun invoke(id: Int): Todo? {
        return repository.getTodoById(id)
    }
}