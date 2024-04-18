package com.example.tickerapp.feature_todo.presentation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tickerapp.feature_todo.presentation.todos.TodosEvent
import com.example.tickerapp.feature_todo.presentation.todos.TodosScreen
import com.example.tickerapp.feature_todo.presentation.todos.TodosViewModel
import com.example.tickerapp.feature_todo.presentation.util.Screen
import com.example.tickerapp.ui.theme.TickerAppTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var viewModel: TodosViewModel
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        if (intent?.hasExtra("isAddingNewTodoIntent") == true) {
            Log.d("MainActivity", "onNewIntent: Key 'isAddingNewTodoIntent' found")
            if (this::viewModel.isInitialized) {
                viewModel.onEvent(TodosEvent.ToggleAddEditBottomSheetModal(null))
            }
        } else {
            Log.d("MainActivity", "onNewIntent: Key 'isAddingNewTodoIntent' not found in intent")
            // Handle the case where the key is missing (optional)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("MainActivity", "onCreate")
        super.onCreate(savedInstanceState)
        var isAddingTodo = false

        if (intent?.hasExtra("isAddingNewTodoIntent") == true) {
            isAddingTodo = intent.getBooleanExtra("isAddingNewTodoIntent", false)
            // ...
        } else {
            Log.d("MainActivity", "Key 'isAddingNewTodoIntent' not found in intent")
            // Handle the case where the key is missing (optional)
        }

        enableEdgeToEdge()
        setContent {
            TickerAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Screen.TodosScreen.route
                    ) {
                        composable(
                            route = Screen.TodosScreen.route
                        ) {
                            TodosScreen(navController = navController, isAddingTodo)
                        }
                    }
                }
            }
        }
    }
}