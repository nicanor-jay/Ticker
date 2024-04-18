package com.example.tickerapp.feature_todo.presentation.util

sealed class Screen(val route: String) {
    object TodosScreen : Screen("todos_screen")
    object AddEditTodoScreen : Screen("add_edit_todo_screen")
}
