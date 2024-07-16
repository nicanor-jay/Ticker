package com.example.tickerapp.feature_todo.presentation.add_edit_todo.components

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tickerapp.R
import com.example.tickerapp.common.utils.Constants
import com.example.tickerapp.feature_todo.domain.model.Priority
import com.example.tickerapp.feature_todo.domain.model.getPriorityColor
import com.example.tickerapp.feature_todo.domain.util.formatDate
import com.example.tickerapp.feature_todo.presentation.add_edit_todo.AddEditTodoEvent
import com.example.tickerapp.feature_todo.presentation.add_edit_todo.AddEditViewModel
import com.example.tickerapp.feature_todo.presentation.util.isSameDay
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditBottomSheetModal(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    todoId: Int?,
    viewModel: AddEditViewModel = hiltViewModel()
) {
    val sheetState = rememberModalBottomSheetState()
    val state by viewModel.state.collectAsState()

    val titleState = state.todo.title
    val priorityState = state.todo.priority
    val dueDateState = state.todo.timeStampDueDate
    val labelState = state.todo.labelString

    val snackbarHostState = remember { SnackbarHostState() }

    val currentTimeStamp = System.currentTimeMillis()

    Log.d(
        "AddEditBottomSheetModal",
        "dueDateState: $dueDateState, formatted: ${formatDate(dueDateState)}"
    )

    val scope = rememberCoroutineScope()
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(todoId) {
        viewModel.onEvent(AddEditTodoEvent.InitTodoId(todoId))
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is AddEditViewModel.UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = event.message,
                    )
                }

                AddEditViewModel.UiEvent.SaveTodo -> {
                    snackbarHostState.showSnackbar(
                        message = "Saved todo",
                    )
                }

                AddEditViewModel.UiEvent.SaveNewLabel -> {
                    snackbarHostState.showSnackbar(
                        message = "Saved new label"
                    )
                }
            }
        }
    }

    ModalBottomSheet(
        onDismissRequest = {
            onDismissRequest()
            viewModel.onEvent(AddEditTodoEvent.ResetAddEditBottomSheetModal)
        }, sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = titleState,
                onValueChange = {
                    if (it.length < Constants.TODO_CHARACTER_LIMIT) {
                        viewModel.onEvent(AddEditTodoEvent.EnteredTitle(it))
                    }
                },
                label = { Text("Add todo") },
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .fillMaxWidth(.8f)
            )

            ExposedDropdownMenuBox(
                expanded = state.isDropdownOpen,
                onExpandedChange = { viewModel.onEvent(AddEditTodoEvent.ToggleDropdown) }
            ) {
                val text =
                    if (priorityState == Priority.DEFAULT) "Priority" else priorityState.displayString
                OutlinedTextField(modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(.8f),
                    readOnly = true,
                    leadingIcon = {
                        if (priorityState != Priority.DEFAULT) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_circle_24),
                                contentDescription = "${priorityState.displayString} color",
                                tint = getPriorityColor(priority = priorityState)
                            )
                        }
                    },
                    value = text,
                    onValueChange = {},
                    label = { },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = state.isDropdownOpen)
                    })
                ExposedDropdownMenu(
                    expanded = state.isDropdownOpen,
                    onDismissRequest = { viewModel.onEvent(AddEditTodoEvent.ToggleDropdown) },
                ) {
                    Priority.entries.forEach { priority ->
                        DropdownMenuItem(text = {
                            Row {
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_circle_24),
                                    "${priority.displayString} color",
                                    tint = getPriorityColor(priority = priority)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(priority.displayString)
                            }
                        }, onClick = {
                            viewModel.onEvent(AddEditTodoEvent.EnteredPriority(priority))
                            viewModel.onEvent(AddEditTodoEvent.ToggleDropdown)
                        }, contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                        )
                    }
                }
            }

            Spacer(modifier = modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(.8f),
//                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Button(
                    onClick = { viewModel.onEvent(AddEditTodoEvent.ToggleDatePickerDialog) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(4.dp),

                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_calendar_month_24),
                        "Calendar icon"
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isSameDay(currentTimeStamp, dueDateState)) {
                            "Today"
                        } else {
                            formatDate(dueDateState, false)
                        },
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 16.sp,
                        ),
                    )
                }
                Spacer(modifier = modifier.width(4.dp))
                Button(
                    onClick = { viewModel.onEvent(AddEditTodoEvent.ToggleLabelModal) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(4.dp),

                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_label_24),
                        "Label icon"
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (labelState == Constants.DEFAULT_LABEL.labelString) {

                            "Label"
                        } else {
                            labelState
                        },
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 16.sp,
                        ),
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { viewModel.onEvent(AddEditTodoEvent.SaveTodo) },
                modifier = modifier.align(Alignment.CenterHorizontally)
            ) {
                if (todoId != null) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_save_24), "Save todo"
                    )
                } else {
                    Icon(Icons.Default.Add, "Add Todo")
                }
            }
            SnackbarHost(snackbarHostState)
        }
        LaunchedEffect(Unit) {
            if (todoId == null) {
                delay(100)
                focusRequester.requestFocus()
            }
        }

        if (state.isDatePickerVisible) {
            val datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = dueDateState,
                selectableDates = object : SelectableDates {
                    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                        return isSameDay(
                            currentTimeStamp, utcTimeMillis
                        ) || utcTimeMillis >= currentTimeStamp
                    }
                },
            )

            DatePickerDialog(onDismissRequest = { viewModel.onEvent(AddEditTodoEvent.ToggleDatePickerDialog) },
                dismissButton = {
                    Button(onClick = { viewModel.onEvent(AddEditTodoEvent.ToggleDatePickerDialog) }) {
                        Text(
                            "Cancel"
                        )
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        viewModel.onEvent(
                            AddEditTodoEvent.EnteredDueDate(
                                datePickerState.selectedDateMillis ?: currentTimeStamp
                            )
                        )
                        viewModel.onEvent(AddEditTodoEvent.ToggleDatePickerDialog)
                    }) { Text("Set") }
                }) {
                DatePicker(state = datePickerState)
            }
        }

        if (state.isLabelModalOpen) {
            LabelModal(
                modifier = Modifier,
                labelList = state.labelList,
                selectedIndex = state.selectedLabelIndex,
                updateSelectedIndex = { value ->
                    viewModel.onEvent(AddEditTodoEvent.EnteredLabel(state.labelList[value]))
                    viewModel.onEvent(AddEditTodoEvent.ToggleLabelModal)
                },
                addNewLabel = { value ->
                    viewModel.onEvent(AddEditTodoEvent.AddNewLabel(value))

                },
                onDismissRequest = {
                    viewModel.onEvent(AddEditTodoEvent.ToggleLabelModal)
                }
            )
        }
    }
}

