package com.example.tickerapp.feature_todo.presentation.add_edit_todo

import com.example.tickerapp.feature_todo.domain.model.Entities.Label
import com.example.tickerapp.feature_todo.domain.model.Priority

sealed class AddEditTodoEvent {
    data class InitTodoId(val todoId: Int?) : AddEditTodoEvent()

    data class EnteredTitle(val value: String) : AddEditTodoEvent()

    data class EnteredPriority(val value: Priority) : AddEditTodoEvent()
    data class EnteredDueDate(val value: Long) : AddEditTodoEvent()
    data class EnteredLabel(val value: Label) : AddEditTodoEvent()

    data class UpdateSelectedLabelIndex(val value: Int) : AddEditTodoEvent()
    data class AddNewLabel(val value: Label) : AddEditTodoEvent()
    object SaveTodo : AddEditTodoEvent()

    object ToggleDropdown : AddEditTodoEvent()

    object ToggleDatePickerDialog : AddEditTodoEvent()

    object ToggleLabelModal : AddEditTodoEvent()

    object ResetAddEditBottomSheetModal : AddEditTodoEvent()

}
