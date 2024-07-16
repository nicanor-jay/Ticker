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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.tickerapp.R
import com.example.tickerapp.feature_todo.domain.model.Entities.Todo
import com.example.tickerapp.feature_todo.domain.model.TodoWithLabel
import com.example.tickerapp.feature_todo.domain.util.formatDate
import com.example.tickerapp.feature_todo.presentation.util.isAfterDay
import com.example.tickerapp.feature_todo.presentation.util.isBeforeDay
import com.example.tickerapp.feature_todo.presentation.util.isSameDay
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TodoInboxSection(
    todosWithLabel: List<TodoWithLabel>,
    snackbarHostState: SnackbarHostState,
    checkboxClick: (Todo) -> Unit,
    deleteTodo: (Todo) -> Unit,
    restoreTodo: () -> Unit,
    toggleModal: (Int) -> Unit,
    rescheduleTodos: (Long) -> Unit,
    isDatePickerVisible: Boolean,
    toggleDatePicker: () -> Unit,
    scope: CoroutineScope
) {
    val currentTimeStamp = System.currentTimeMillis()
    val density = LocalDensity.current

    val hasOverdueTodos = todosWithLabel.any {
        !it.todo.isCompleted && isBeforeDay(
            it.todo.timeStampDueDate,
            currentTimeStamp,
        )
    }

    if (todosWithLabel.none { !it.todo.isCompleted }) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = stringResource(R.string.no_todos_description),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )
        }
        return
    }

    if (isDatePickerVisible) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = currentTimeStamp,
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    return isSameDay(
                        currentTimeStamp, utcTimeMillis
                    ) || utcTimeMillis >= currentTimeStamp
                }
            }
        )

        DatePickerDialog(onDismissRequest = { toggleDatePicker() },
            dismissButton = {
                Button(onClick = { toggleDatePicker() }) {
                    Text(
                        "Cancel"
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    rescheduleTodos(datePickerState.selectedDateMillis ?: currentTimeStamp)
                    toggleDatePicker()
                }) { Text("Set") }
            }) {
            DatePicker(state = datePickerState)
        }
    }

    LazyColumn(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {

        //Overdue Section
        item(key = "overdue_section_separator") {
            AnimatedVisibility(
                modifier = Modifier.animateItemPlacement(
                    animationSpec = tween(
                        durationMillis = 100,
                        easing = FastOutSlowInEasing
                    )
                ),
                visible = hasOverdueTodos,
                enter = slideInVertically {
                    // Slide in from 40 dp from the top.
                    with(density) { -40.dp.roundToPx() }
                } + fadeIn(
                    // Fade in with the initial alpha of 0.3f.
                    initialAlpha = 0.3f
                ),
                exit = slideOutVertically() + fadeOut()
            ) {

                TodosSeparator(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    text = stringResource(id = R.string.overdue_label)
                )
            }
        }

        items(todosWithLabel.filter {
            isBeforeDay(
                it.todo.timeStampDueDate,
                currentTimeStamp,
            )
        }, key = { it.todo.id!! }
        ) { todoWithLabel ->
            AnimatedVisibility(
                modifier = Modifier.animateItemPlacement(
                    animationSpec = tween(
                        durationMillis = 100,
                        easing = FastOutSlowInEasing
                    )
                ),
                visible = !todoWithLabel.todo.isCompleted,
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
                            toggleModal(todoWithLabel.todo.id!!)
                        }
                )
            }
        }

        item(key = "reschedule_button") {
            AnimatedVisibility(
                modifier = Modifier.animateItemPlacement(
                    animationSpec = tween(
                        durationMillis = 100,
                        easing = FastOutSlowInEasing
                    )
                ),
                visible = hasOverdueTodos,
                enter = slideInVertically {
                    // Slide in from 40 dp from the top.
                    with(density) { -40.dp.roundToPx() }
                } + fadeIn(
                    // Fade in with the initial alpha of 0.3f.
                    initialAlpha = 0.3f
                ),
                exit = slideOutVertically() + fadeOut()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    // Center the button in the middle horizontally within the row
                    TextButton(onClick = { toggleDatePicker() }) {
                        Text(
                            text = "Reschedule All",
                            style = MaterialTheme.typography.bodyLarge,
//                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        //Today Section
        item(key = "today_section_separator") {
            AnimatedVisibility(
                modifier = Modifier.animateItemPlacement(
                    animationSpec = tween(
                        durationMillis = 100,
                        easing = FastOutSlowInEasing
                    )
                ),
                visible = todosWithLabel.any {
                    !it.todo.isCompleted && isSameDay(
                        it.todo.timeStampDueDate,
                        currentTimeStamp
                    )
                },
                enter = slideInVertically {
                    // Slide in from 40 dp from the top.
                    with(density) { -40.dp.roundToPx() }
                } + fadeIn(
                    // Fade in with the initial alpha of 0.3f.
                    initialAlpha = 0.3f
                ),
                exit = slideOutVertically() + fadeOut()
            ) {

                TodosSeparator(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    text = formatDate(currentTimeStamp)
                )
            }
        }

        items(todosWithLabel.filter {
            isSameDay(
                currentTimeStamp,
                it.todo.timeStampDueDate
            )
        }, key = { it.todo.id!! }) { todoWithLabel ->

            AnimatedVisibility(
                modifier = Modifier.animateItemPlacement(
                    animationSpec = tween(
                        durationMillis = 100,
                        easing = FastOutSlowInEasing
                    )
                ),
                visible = !todoWithLabel.todo.isCompleted,
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
                            toggleModal(todoWithLabel.todo.id!!)
                        }
                )
            }
        }

        //Upcoming Section
        item(key = "upcoming_section_separator") {
            AnimatedVisibility(
                modifier = Modifier.animateItemPlacement(
                    animationSpec = tween(
                        durationMillis = 100,
                        easing = FastOutSlowInEasing
                    )
                ),
                visible = todosWithLabel.any {
                    !it.todo.isCompleted && isAfterDay(
                        it.todo.timeStampDueDate,
                        currentTimeStamp,
                    )
                },
                enter = slideInVertically {
                    // Slide in from 40 dp from the top.
                    with(density) { -40.dp.roundToPx() }
                } + fadeIn(
                    // Fade in with the initial alpha of 0.3f.
                    initialAlpha = 0.3f
                ),
                exit = slideOutVertically() + fadeOut()
            ) {

                TodosSeparator(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    text = stringResource(id = R.string.upcoming_label)
                )
            }
        }

        items(todosWithLabel.filter {
            isAfterDay(
                it.todo.timeStampDueDate,
                currentTimeStamp,
            )
        }, key = { it.todo.id!! }) { todoWithLabel ->

            AnimatedVisibility(
                modifier = Modifier.animateItemPlacement(
                    animationSpec = tween(
                        durationMillis = 100,
                        easing = FastOutSlowInEasing
                    )
                ),
                visible = !todoWithLabel.todo.isCompleted,
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
                            toggleModal(todoWithLabel.todo.id!!)
                        }
                )
            }
        }
    }
}