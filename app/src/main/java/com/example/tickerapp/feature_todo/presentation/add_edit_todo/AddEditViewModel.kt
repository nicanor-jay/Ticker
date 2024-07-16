package com.example.tickerapp.feature_todo.presentation.add_edit_todo

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tickerapp.feature_todo.domain.model.Entities.InvalidLabelException
import com.example.tickerapp.feature_todo.domain.model.Entities.InvalidTodoException
import com.example.tickerapp.feature_todo.domain.use_case.Labels.LabelUseCases
import com.example.tickerapp.feature_todo.domain.use_case.Todos.TodoUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditViewModel @Inject constructor(
    private val todoUseCases: TodoUseCases,
    private val labelUseCases: LabelUseCases,
) : ViewModel() {

    private val _currentTodoId = MutableStateFlow<Int?>(null)
    private val currentTodoId: StateFlow<Int?> = _currentTodoId

    private val _state = MutableStateFlow(AddEditModalState())
    val state: StateFlow<AddEditModalState> = _state

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()


    init {
        viewModelScope.launch {
            labelUseCases.getLabels().collect { list ->
                _state.value = state.value.copy(labelList = list)
            }
        }

        viewModelScope.launch {
            currentTodoId.collect { todoId ->
                if (todoId != null) {
                    _currentTodoId.value?.let {
                        todoUseCases.getTodoWithLabel(it)?.also { todo ->
                            _state.value = state.value.copy(
                                todo = todo.toTodoWithLabelState(),
                                selectedLabelIndex = 1
                            )
                        }
                    }
                }
            }
        }

    }

    private fun resetValues() {
        _currentTodoId.value = null
        _state.value = AddEditModalState(
            todo = TodoWithLabelState(),
            labelList = state.value.labelList,
        )

    }

    fun onEvent(event: AddEditTodoEvent) {
        when (event) {
            is AddEditTodoEvent.InitTodoId -> {
                _currentTodoId.value = event.todoId
                Log.d("AddEditBottomSheetModal", "Setting todoId to ${event.todoId}")
            }

            is AddEditTodoEvent.EnteredTitle -> {
                _state.value = state.value.copy(
                    todo = state.value.todo.copy(
                        title = event.value
                    )
                )
            }

            is AddEditTodoEvent.EnteredPriority -> {

                _state.value = state.value.copy(
                    todo = state.value.todo.copy(
                        priority = event.value
                    )
                )
            }

            is AddEditTodoEvent.EnteredDueDate -> {

                _state.value = state.value.copy(
                    todo = state.value.todo.copy(
                        timeStampDueDate = event.value
                    )
                )
            }

            is AddEditTodoEvent.EnteredLabel -> {
                _state.value = state.value.copy(
                    todo = state.value.todo.copy(
                        labelString = event.value.labelString,
                        labelId = event.value.id!!
                    )
                )
            }

            is AddEditTodoEvent.SaveTodo -> {
                viewModelScope.launch {
                    try {
                        todoUseCases.addTodo(
                            state.value.todo.toTodoWithLabel().todo
                        )
                        resetValues()
                        _eventFlow.emit(
                            UiEvent.SaveTodo
                        )
                    } catch (e: InvalidTodoException) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                message = e.message ?: "Couldn't save todo"
                            )
                        )
                    }
                }
            }

            is AddEditTodoEvent.AddNewLabel -> {
                viewModelScope.launch {
                    try {
                        labelUseCases.addLabel(event.value)
                        _eventFlow.emit(
                            UiEvent.SaveNewLabel
                        )
                    } catch (e: InvalidLabelException) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                message = e.message ?: "Couldn't save new label"
                            )
                        )
                    }
                }
            }

            is AddEditTodoEvent.ToggleDropdown -> {
                _state.value = state.value.copy(
                    isDropdownOpen = !state.value.isDropdownOpen
                )
            }

            is AddEditTodoEvent.ToggleDatePickerDialog -> {
                _state.value = state.value.copy(
                    isDatePickerVisible = !state.value.isDatePickerVisible
                )
            }

            is AddEditTodoEvent.ToggleLabelModal -> {
                _state.value = state.value.copy(
                    isLabelModalOpen = !state.value.isLabelModalOpen
                )
            }

            is AddEditTodoEvent.UpdateSelectedLabelIndex -> {
                _state.value = state.value.copy(
                    selectedLabelIndex = event.value
                )

            }

            is AddEditTodoEvent.ResetAddEditBottomSheetModal -> {
                resetValues()
            }
        }
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        object SaveTodo : UiEvent()

        object SaveNewLabel : UiEvent()
    }

}