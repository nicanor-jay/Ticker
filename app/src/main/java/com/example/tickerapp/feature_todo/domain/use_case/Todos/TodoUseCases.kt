package com.example.tickerapp.feature_todo.domain.use_case.Todos

data class TodoUseCases(
    val getTodos: GetTodos,
    val getTodo: GetTodo,
    val getTodosWithLabel: GetTodosWithLabel,
    val getTodoWithLabel: GetTodoWithLabel,
    val deleteTodo: DeleteTodo,
    val toggleCompleted: ToggleCompleted,
    val addTodo: AddTodo,
)
