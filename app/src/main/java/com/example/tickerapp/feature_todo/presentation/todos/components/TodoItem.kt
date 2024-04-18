package com.example.tickerapp.feature_todo.presentation.todos.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tickerapp.R
import com.example.tickerapp.common.utils.Constants
import com.example.tickerapp.feature_todo.domain.model.Entities.Label
import com.example.tickerapp.feature_todo.domain.model.Entities.Todo
import com.example.tickerapp.feature_todo.domain.model.Priority
import com.example.tickerapp.feature_todo.domain.model.TodoWithLabel
import com.example.tickerapp.feature_todo.domain.model.getPriorityColor
import com.example.tickerapp.feature_todo.domain.util.formatDate
import com.example.tickerapp.feature_todo.presentation.util.isAfterDay
import com.example.tickerapp.feature_todo.presentation.util.isSameDay
import com.example.tickerapp.ui.theme.Red
import com.example.tickerapp.ui.theme.TickerAppTheme

@Composable
fun TodoItem(
    todoWithLabel: TodoWithLabel,
    modifier: Modifier = Modifier,
    onCheckboxClick: (Boolean) -> Unit,
    onDeleteClick: () -> Unit
) {
    val currentTimeStamp = System.currentTimeMillis()

    val todoState = todoWithLabel.todo
    val labelState = todoWithLabel.label

    val paddingBottom = if (labelState == Constants.DEFAULT_LABEL && isSameDay(
            currentTimeStamp,
            todoState.timeStampDueDate
        )
    ) 0.dp else 12.dp
    val paddingTop = if (labelState == Constants.DEFAULT_LABEL && isSameDay(
            currentTimeStamp,
            todoState.timeStampDueDate
        )
    ) 0.dp else 22.dp

    val lineThrough = if (todoState.isCompleted) TextDecoration.LineThrough else TextDecoration.None
    val checkboxColors = CheckboxDefaults.colors(
        uncheckedColor = getPriorityColor(todoState.priority),
        checkedColor = getPriorityColor(todoState.priority),
        checkmarkColor = Color.White

    )
    val titleColor =
        if (!todoState.isCompleted) MaterialTheme.typography.bodyLarge.color else Color.Gray


    ElevatedCard(onClick = { /*TODO*/ }, modifier = modifier.padding(vertical = 8.dp)) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(bottom = paddingBottom),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(vertical = 2.dp)
                    .wrapContentWidth()
            ) {
                Checkbox(
                    checked = todoState.isCompleted,
                    onCheckedChange = onCheckboxClick,
                    colors = checkboxColors
                )
                Column(modifier = modifier.weight(1f)) {
                    Text(
                        todoState.title,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            textDecoration = lineThrough, fontSize = 18.sp, color = titleColor
                        ),
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(
                            top = paddingTop
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (todoState.isCompleted) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_calendar_month_24),
                                "Calendar icon",
                                modifier = Modifier.size(16.dp),
                                tint = MaterialTheme.colorScheme.secondary
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = formatDate(
                                    todoState.timestampCompleted,
                                    false
                                ),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.secondary,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        } else {
                            if (!isSameDay(todoState.timeStampDueDate, currentTimeStamp)) {
                                val color = if (isAfterDay(
                                        todoState.timeStampDueDate,
                                        currentTimeStamp
                                    )
                                ) MaterialTheme.colorScheme.secondary else Red
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_calendar_month_24),
                                    "Calendar icon",
                                    modifier = Modifier.size(16.dp),
                                    tint = color
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(
                                    text = formatDate(
                                        todoState.timeStampDueDate,
                                        false
                                    ),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = color,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                            }
                        }
                        if (labelState != Constants.DEFAULT_LABEL) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_label_24),
                                "Calendar icon",
                                modifier = Modifier.size(16.dp),
                                tint = MaterialTheme.colorScheme.secondary
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                labelState.labelString,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.secondary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
                IconButton(onClick = onDeleteClick) {
                    Icon(Icons.Default.Delete, "Delete Todo")
                }
            }
        }
    }
}


@Preview
@Composable
fun TodoItemUncheckedDefaultUnchecked() {
    TickerAppTheme {
        TodoItem(
            TodoWithLabel(
                Todo(null, "Clean room", Priority.DEFAULT, 1, false, 0, 0, 0),
                Label(1, "None (default)")
            ), modifier = Modifier, {}, {})
    }
}

@Preview
@Composable
fun TodoItemCheckedDefaultUnchecked() {
    TickerAppTheme {
        TodoItem(
            TodoWithLabel(
                Todo(
                    null, "Clean room", Priority.DEFAULT, 1, true, 0, 0, 0
                ), Label(1, "None (default)")
            ), modifier = Modifier, {}, {})
    }
}

@Preview
@Composable
fun TodoItemUncheckedLowPreview() {
    TickerAppTheme {
        TodoItem(
            TodoWithLabel(
                Todo(
                    null, "Clean room", Priority.LOW, 1, false, 0, 0, 0
                ), Label(1, "None (default)")
            ), modifier = Modifier, {}, {})
    }
}


@Preview
@Composable
fun TodoItemCheckedLowLabelPreview() {
    TickerAppTheme {
        TodoItem(
            TodoWithLabel(
                Todo(
                    null, "Clean room", Priority.LOW, 2, true, 0, 0, 0
                ), Label(2, "Work")
            ), modifier = Modifier, {}, {})
    }
}

@Preview
@Composable
fun TodoItemUncheckedMediumLabelPreview() {
    TickerAppTheme {
        TodoItem(
            TodoWithLabel(
                Todo(
                    null, "Clean room", Priority.MEDIUM, 3, false, 0, 0, 0
                ), Label(3, "Personal")
            ), modifier = Modifier, {}, {})
    }
}

@Preview
@Composable
fun TodoItemCheckedMediumLabelPreview() {
    TickerAppTheme {
        TodoItem(
            TodoWithLabel(
                Todo(
                    null, "Clean room", Priority.MEDIUM, 3, true, 0, 0, 0
                ), Label(3, "Personal")
            ), modifier = Modifier, {}, {})
    }
}

@Preview
@Composable
fun TodoItemUncheckedHighLabelPreview() {
    TickerAppTheme {
        TodoItem(
            TodoWithLabel(
                Todo(
                    null, "Clean room", Priority.HIGH, 2, false, 0, 0, 0
                ), Label(2, "Work")
            ), modifier = Modifier, {}, {})
    }
}

@Preview
@Composable
fun TodoItemCheckedHighLabelPreview() {
    TickerAppTheme {
        TodoItem(
            TodoWithLabel(
                Todo(
                    null, "Clean room", Priority.HIGH, 2, true, 0, 0, 0
                ), Label(2, "Work")
            ), modifier = Modifier, {}, {})
    }
}

@Preview
@Composable
fun TodoItemCheckedHighLabelLongTodoPreview() {
    TickerAppTheme {
        TodoItem(
            TodoWithLabel(
                Todo(
                    null,
                    "Clean room this afternoon before in-laws arrive",
                    Priority.HIGH,
                    2,
                    true,
                    0,
                    0,
                    0
                ), Label(2, "Work")
            ), modifier = Modifier, {}, {})
    }
}