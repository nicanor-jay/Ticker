package com.example.tickerapp.feature_todo.domain.repository

import com.example.tickerapp.feature_todo.domain.model.Entities.Todo
import com.example.tickerapp.feature_todo.domain.model.TodoWithLabel
import kotlinx.coroutines.flow.Flow

interface TodoRepository {

    fun getTodos(): Flow<List<Todo>>
    fun getTodosWithLabel(): Flow<List<TodoWithLabel>>

    suspend fun getTodoById(id: Int): Todo?
    suspend fun getTodoWithLabelById(id: Int): TodoWithLabel?

    suspend fun insertTodo(todo: Todo)

    suspend fun deleteTodo(todo: Todo)

    suspend fun toggleCompleted(todo: Todo)
}