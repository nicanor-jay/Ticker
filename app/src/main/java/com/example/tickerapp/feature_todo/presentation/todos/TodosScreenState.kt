package com.example.tickerapp.feature_todo.presentation.todos

import com.example.tickerapp.feature_todo.domain.model.Entities.Label
import com.example.tickerapp.feature_todo.domain.model.OrderOptions
import com.example.tickerapp.feature_todo.domain.model.TodoWithLabel
import com.example.tickerapp.feature_todo.domain.util.OrderDirection

data class TodosScreenState(
    val todos: List<TodoWithLabel> = emptyList(),

    val todoOrderOptionPreference: OrderOptions = OrderOptions.DATE,
    val todoOrderDirectionPreference: OrderDirection = OrderDirection.ASCENDING,

    val isOrderSectionVisible: Boolean = false,
    val isMenuDropdownOpen: Boolean = false,
    val isSortDropdownOpen: Boolean = false,
    val isAddEditBottomSheetModalVisible: Boolean = false,
    val isEditLabelsModalVisible: Boolean = false,
    val currentTodoId: Int? = null,
    val showCompletedPreference: Boolean = false,

    val labelsList: List<Label> = emptyList(),
    val isDeleteLabelConfirmationVisible: Boolean = false,
    val selectedLabelForDeletion: Label = Label(labelString = "")
)