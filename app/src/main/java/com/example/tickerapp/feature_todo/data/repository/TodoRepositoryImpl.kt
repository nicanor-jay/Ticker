package com.example.tickerapp.feature_todo.data.repository

import com.example.tickerapp.feature_todo.data.data_source.TodoDao
import com.example.tickerapp.feature_todo.domain.model.Entities.Todo
import com.example.tickerapp.feature_todo.domain.model.TodoWithLabel
import com.example.tickerapp.feature_todo.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow

class TodoRepositoryImpl(
    private val dao: TodoDao
) : TodoRepository {
    override fun getTodos(): Flow<List<Todo>> {
        return dao.getTodos()
    }

    override fun getTodosWithLabel(): Flow<List<TodoWithLabel>> {
        return dao.getTodosWithLabel()
    }

    override suspend fun getTodoById(id: Int): Todo? {
        return dao.getTodoById(id)
    }

    override suspend fun getTodoWithLabelById(id: Int): TodoWithLabel? {
        return dao.getTodoWithLabelById(id)
    }

    override suspend fun insertTodo(todo: Todo) {
        dao.insertTodo(todo)
    }

    override suspend fun deleteTodo(todo: Todo) {
        dao.deleteTodo(todo)
    }

    override suspend fun toggleCompleted(todo: Todo) {
        dao.insertTodo(todo.copy(isCompleted = !todo.isCompleted))
    }

}