package com.example.tickerapp.feature_todo.presentation.add_edit_todo

import com.example.tickerapp.common.utils.Constants
import com.example.tickerapp.feature_todo.domain.model.Entities.Label
import com.example.tickerapp.feature_todo.domain.model.Entities.Todo
import com.example.tickerapp.feature_todo.domain.model.Priority
import com.example.tickerapp.feature_todo.domain.model.TodoWithLabel

data class TodoWithLabelState(
    val id: Int? = null,
    val title: String = "",
    val priority: Priority = Priority.DEFAULT,
    val labelId: Int = Constants.DEFAULT_LABEL.id ?: 1,
    val labelString: String = Constants.DEFAULT_LABEL.labelString,
    val isCompleted: Boolean = false,
    val timeStampCreated: Long = System.currentTimeMillis(),
    val timeStampDueDate: Long = System.currentTimeMillis(),
    val timeStampCompleted: Long = 0
)

fun TodoWithLabelState.toTodoWithLabel(): TodoWithLabel {
    return TodoWithLabel(
        todo = Todo(
            id = this.id,
            title = this.title,
            priority = this.priority,
            labelId = this.labelId, // You haven't specified label in TodoState, so assuming it's an empty string
            isCompleted = this.isCompleted,
            timestampCreated = this.timeStampCreated,
            timeStampDueDate = this.timeStampDueDate,
            timestampCompleted = this.timeStampCompleted
        ),
        label = Label(
            id = this.labelId,
            labelString = this.labelString
        )
    )
}

fun TodoWithLabel.toTodoWithLabelState(): TodoWithLabelState {
    return TodoWithLabelState(
        id = this.todo.id,
        title = this.todo.title,
        priority = this.todo.priority,
        labelId = this.label.id ?: 0,
        labelString = this.label.labelString,
        isCompleted = this.todo.isCompleted,
        timeStampCreated = this.todo.timestampCreated,
        timeStampDueDate = this.todo.timeStampDueDate,
        timeStampCompleted = this.todo.timestampCompleted
    )
}