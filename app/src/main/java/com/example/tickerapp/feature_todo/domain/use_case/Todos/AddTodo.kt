package com.example.tickerapp.feature_todo.domain.use_case.Todos

import com.example.tickerapp.feature_todo.domain.model.Entities.InvalidTodoException
import com.example.tickerapp.feature_todo.domain.model.Entities.Todo
import com.example.tickerapp.feature_todo.domain.repository.TodoRepository

class AddTodo(
    private val repository: TodoRepository
) {
    suspend operator fun invoke(todo: Todo) {
        if (todo.title.isBlank()) {
            throw InvalidTodoException("The title of the todo cannot be empty")
        }
        repository.insertTodo(todo)
    }
}