package com.example.tickerapp.feature_todo.presentation.todos

import com.example.tickerapp.feature_todo.domain.model.Entities.Label
import com.example.tickerapp.feature_todo.domain.model.Entities.Todo
import com.example.tickerapp.feature_todo.domain.model.OrderOptions
import com.example.tickerapp.feature_todo.presentation.add_edit_todo.AddEditTodoEvent
import com.example.tickerapp.feature_todo.presentation.util.TodoSections

sealed class TodosEvent {
    data class DeleteTodo(val todo: Todo) : TodosEvent()
    data class ToggleCompleted(val todo: Todo) : TodosEvent()
    object RestoreTodo : TodosEvent()
    object ToggleOrderSection : TodosEvent()
    object ToggleMenuDropdown : TodosEvent()
    object ToggleEditLabelModal : TodosEvent()
    data class RescheduleTodos(val newDueDate: Long) : TodosEvent()
    object ToggleDatePickerDialog : TodosEvent()
    data class ToggleAddEditBottomSheetModal(val todoId: Int?) : TodosEvent()
    data object ToggleSortDropdown : TodosEvent()
    data class ToggleSection(val section: TodoSections) : TodosEvent()

    //Preferences Change
    data class ToggleUserPreference(val preferenceKey: String) : TodosEvent()
    data class ChangeOrderOption(val orderOption: OrderOptions) : TodosEvent()
    data class AddLabel(val label: Label) : TodosEvent()
    data class UpdateLabel(val label: Label) : TodosEvent()

    data class UpdateSelectedLabelToDelete(val label: Label) : TodosEvent()
    object DeleteSelectedLabel : TodosEvent()
    object ToggleDeleteLabelDialog : TodosEvent()
}