package com.example.tickerapp.feature_todo.presentation.todos.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.tickerapp.R
import com.example.tickerapp.feature_todo.domain.model.Entities.Todo
import com.example.tickerapp.feature_todo.domain.model.TodoWithLabel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TodoIsCompletedSection(
    todosWithLabel: List<TodoWithLabel>,
    snackbarHostState: SnackbarHostState,
    checkboxClick: (Todo) -> Unit,
    deleteTodo: (Todo) -> Unit,
    restoreTodo: () -> Unit,
    toggleModal: (Int) -> Unit,
    scope: CoroutineScope
) {
    val density = LocalDensity.current

    if (todosWithLabel.none { it.todo.isCompleted }) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = stringResource(R.string.no_completed_todos_description),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )
        }
        return
    }

    LazyColumn(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        items(
            todosWithLabel.sortedByDescending { it.todo.timestampCompleted },
            key = { it.todo.id!! }) { todoWithLabel ->
            val todoState = todoWithLabel.todo

            AnimatedVisibility(
                modifier = Modifier.animateItemPlacement(
                    animationSpec = tween(
                        durationMillis = 100,
                        easing = FastOutSlowInEasing
                    )
                ),
                visible = todoState.isCompleted,
                enter = slideInVertically {
                    // Slide in from 40 dp from the top.
                    with(density) { -40.dp.roundToPx() }
                } + fadeIn(
                    // Fade in with the initial alpha of 0.3f.
                    initialAlpha = 0.3f
                ),
                exit = slideOutVertically() + fadeOut()
            ) {
                TodoItem(
                    todoWithLabel = todoWithLabel,
                    onCheckboxClick = {
                        checkboxClick(todoWithLabel.todo)
                    },
                    onDeleteClick = {
                        deleteTodo(todoWithLabel.todo)
                        scope.launch {
                            val result = snackbarHostState.showSnackbar(
                                message = "Todo deleted",
                                actionLabel = "Undo"
                            )
                            if (result == SnackbarResult.ActionPerformed) {
                                restoreTodo()
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            toggleModal(todoState.id!!)
                        }
                )
            }
        }
    }
}