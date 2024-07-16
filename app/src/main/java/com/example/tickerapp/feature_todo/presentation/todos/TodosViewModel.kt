package com.example.tickerapp.feature_todo.presentation.todos

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tickerapp.common.utils.Constants
import com.example.tickerapp.feature_todo.domain.model.Entities.Label
import com.example.tickerapp.feature_todo.domain.model.Entities.Todo
import com.example.tickerapp.feature_todo.domain.model.OrderOptions
import com.example.tickerapp.feature_todo.domain.use_case.Labels.LabelUseCases
import com.example.tickerapp.feature_todo.domain.use_case.Preferences.PreferenceUseCases
import com.example.tickerapp.feature_todo.domain.use_case.Todos.TodoUseCases
import com.example.tickerapp.feature_todo.domain.util.DataStoreResult
import com.example.tickerapp.feature_todo.domain.util.OrderDirection
import com.example.tickerapp.feature_todo.presentation.util.TodoSections
import com.example.tickerapp.feature_todo.presentation.util.isBeforeDay
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodosViewModel @Inject constructor(
    private val todoUseCases: TodoUseCases,
    private val preferencesUseCases: PreferenceUseCases,
    private val labelUseCases: LabelUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(TodosScreenState())
    val state: StateFlow<TodosScreenState> = _state

    private val _currentSection = MutableStateFlow(TodoSections.INBOX)
    val currentSection: StateFlow<TodoSections> = _currentSection

    private var recentlyDeletedTodo: Todo? = null

    private var getTodosJob: Job? = null

    init {
        getTodos(OrderDirection.ASCENDING, OrderOptions.DATE)

        viewModelScope.launch {
            labelUseCases.getLabels().collect { list ->
                _state.value = state.value.copy(labelsList = list)
            }
        }

        viewModelScope.launch {
            preferencesUseCases.observeString(Constants.ORDER_TYPE_KEY, OrderOptions.DATE.name)
                .collect {
                    Log.d("TodosViewModel", "New ORDER_TYPE_KEY value emitted $it")
                    _state.value =
                        state.value.copy(todoOrderOptionPreference = OrderOptions.valueOf(it))
                }
        }
        viewModelScope.launch {
            preferencesUseCases.observeString(
                Constants.ORDER_DIRECTION_KEY,
                OrderDirection.DESCENDING.name
            ).collect {
                Log.d("TodosViewModel", "New ORDER_DIRECTION_KEY value emitted $it")
                _state.value =
                    state.value.copy(todoOrderDirectionPreference = OrderDirection.valueOf(it))
            }
        }

    }

    fun onEvent(event: TodosEvent) {
        when (event) {
            is TodosEvent.ChangeOrderOption -> {
                if (state.value.todoOrderOptionPreference == event.orderOption) {
                    // Also reverse direction
                    val oppositeOrderDirection =
                        if (state.value.todoOrderDirectionPreference == OrderDirection.ASCENDING) {
                            OrderDirection.DESCENDING
                        } else {
                            OrderDirection.ASCENDING
                        }

                    viewModelScope.launch {
                        preferencesUseCases.putString(
                            Constants.ORDER_DIRECTION_KEY,
                            oppositeOrderDirection.name
                        )
                        getTodos(
                            oppositeOrderDirection,
                            state.value.todoOrderOptionPreference
                        )
                    }
                } else {
                    viewModelScope.launch {
                        preferencesUseCases.putString(
                            Constants.ORDER_TYPE_KEY,
                            event.orderOption.name
                        )
                        getTodos(
                            state.value.todoOrderDirectionPreference,
                            event.orderOption
                        )
                    }
                }
            }

            is TodosEvent.DeleteTodo -> {
                viewModelScope.launch {
                    todoUseCases.deleteTodo(event.todo)
                    recentlyDeletedTodo = event.todo
                }
            }

            is TodosEvent.RestoreTodo -> {
                viewModelScope.launch {
                    todoUseCases.addTodo(recentlyDeletedTodo ?: return@launch)
                    recentlyDeletedTodo = null
                }
            }

            is TodosEvent.ToggleCompleted -> {
                viewModelScope.launch {
                    todoUseCases.toggleCompleted(event.todo)
                }
            }

            is TodosEvent.ToggleOrderSection -> {
                _state.value = state.value.copy(
                    isOrderSectionVisible = !state.value.isOrderSectionVisible
                )
            }

            is TodosEvent.ToggleAddEditBottomSheetModal -> {
                val todoId = if (state.value.isAddEditBottomSheetModalVisible) -1 else event.todoId
                _state.value = state.value.copy(
                    isAddEditBottomSheetModalVisible = !state.value.isAddEditBottomSheetModalVisible,
                    currentTodoId = todoId
                )
            }

            is TodosEvent.ToggleMenuDropdown -> {
                _state.value = state.value.copy(
                    isMenuDropdownOpen = !state.value.isMenuDropdownOpen
                )
            }

            is TodosEvent.ToggleSortDropdown -> {
                _state.value = state.value.copy(
                    isSortDropdownOpen = !state.value.isSortDropdownOpen
                )
            }

            is TodosEvent.ToggleEditLabelModal -> {
                _state.value = state.value.copy(
                    isEditLabelsModalVisible = !state.value.isEditLabelsModalVisible
                )
            }

            TodosEvent.ToggleDeleteLabelDialog -> {
                _state.value = state.value.copy(
                    isDeleteLabelConfirmationVisible = !state.value.isDeleteLabelConfirmationVisible
                )

                if (!_state.value.isDeleteLabelConfirmationVisible) {
                    _state.value = state.value.copy(
                        selectedLabelForDeletion = Label(labelString = "")
                    )
                }
            }

            is TodosEvent.UpdateSelectedLabelToDelete -> {
                _state.value = state.value.copy(selectedLabelForDeletion = event.label)
            }

            is TodosEvent.ToggleSection -> {
                _currentSection.value = event.section
            }

            is TodosEvent.ToggleUserPreference -> {
                viewModelScope.launch {
                    val newPreferenceValue = !state.value.showCompletedPreference
                    val result = preferencesUseCases.putBoolean(
                        event.preferenceKey,
                        newPreferenceValue
                    )

                    when (result) {
                        is DataStoreResult.Success -> {
                            Log.d(
                                "TodosViewModel",
                                "SUCCESSFULLY toggled ${event.preferenceKey} preference"
                            )
                        }

                        is DataStoreResult.Error -> {
                            Log.e(
                                "TodosViewModel",
                                "ERROR toggling ${event.preferenceKey} preference",
                                result.exception
                            )
                        }
                    }
                }
            }

            is TodosEvent.AddLabel -> {
                viewModelScope.launch {
                    labelUseCases.addLabel(event.label)
                }
            }

            is TodosEvent.DeleteSelectedLabel -> {
                viewModelScope.launch {
                    labelUseCases.deleteLabel(state.value.selectedLabelForDeletion)
                }
            }

            is TodosEvent.UpdateLabel -> {
                viewModelScope.launch {
                    labelUseCases.updateLabel(event.label)
                }
            }

            is TodosEvent.ToggleDatePickerDialog -> {
                _state.value = state.value.copy(
                    isDatePickerVisible = !state.value.isDatePickerVisible
                )
            }

            is TodosEvent.RescheduleTodos -> {
                Log.d("TodosViewModel", "Rescheduling all overdue todos")
                val currentTimeStamp = System.currentTimeMillis()
                val overdueTodos = state.value.todos.filter {
                    isBeforeDay(
                        it.todo.timeStampDueDate,
                        currentTimeStamp
                    )
                }

                viewModelScope.launch {
                    for (todoWithLabel in overdueTodos) {
                        todoUseCases.addTodo(todoWithLabel.todo.copy(timeStampDueDate = event.newDueDate))
                    }
                }
            }
        }
    }

    private fun getTodos(orderDirection: OrderDirection, orderOption: OrderOptions) {
        getTodosJob?.cancel()
        getTodosJob = todoUseCases.getTodosWithLabel(orderDirection, orderOption)
            .onEach { todos ->
                _state.value = state.value.copy(
                    todos = todos,
                    todoOrderDirectionPreference = orderDirection,
                    todoOrderOptionPreference = orderOption
                )
            }
            .launchIn(viewModelScope)
    }
}

