package com.example.tickerapp.feature_todo.presentation.add_edit_todo

import com.example.tickerapp.feature_todo.domain.model.Entities.Label

data class AddEditModalState(
    val todo: TodoWithLabelState = TodoWithLabelState(),
    val isDropdownOpen: Boolean = false,
    val isDatePickerVisible: Boolean = false,
    val isLabelModalOpen: Boolean = false,
    val selectedLabelIndex: Int = 0,
    val labelList: List<Label> = emptyList(),

    )