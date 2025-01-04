package com.example.tickerapp.feature_todo.presentation.todos

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.tickerapp.R
import com.example.tickerapp.common.composables.TickerTopAppBar
import com.example.tickerapp.feature_todo.domain.model.OrderOptions
import com.example.tickerapp.feature_todo.domain.util.OrderDirection
import com.example.tickerapp.feature_todo.presentation.add_edit_todo.components.AddEditBottomSheetModal
import com.example.tickerapp.feature_todo.presentation.todos.components.ConfigureLabelModal
import com.example.tickerapp.feature_todo.presentation.todos.components.DeleteLabelConfirmationDialog
import com.example.tickerapp.feature_todo.presentation.todos.components.TodoInboxSection
import com.example.tickerapp.feature_todo.presentation.todos.components.TodoIsCompletedSection
import com.example.tickerapp.feature_todo.presentation.util.TodoSections
import kotlinx.coroutines.launch

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class
)
@Composable
fun TodosScreen(
    navController: NavController,
    isAddingNewTodo: Boolean,
    viewModel: TodosViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val currentSection by viewModel.currentSection.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(isAddingNewTodo) {
        if (isAddingNewTodo) {
            viewModel.onEvent(TodosEvent.ToggleAddEditBottomSheetModal(null))
        }
    }

    Scaffold(topBar = {
        TickerTopAppBar(title = stringResource(id = R.string.app_name),
            canNavigateBack = false,
            actions = {
                Box {
                    IconButton(onClick = {
                        viewModel.onEvent(TodosEvent.ToggleMenuDropdown)
                    }) {
                        Icon(
                            Icons.Default.Menu, contentDescription = "Menu button"
                        )
                    }
                    DropdownMenu(expanded = state.isMenuDropdownOpen, onDismissRequest = {
                        viewModel.onEvent(TodosEvent.ToggleMenuDropdown)
                    }) {
                        DropdownMenuItem(text = { Text(stringResource(R.string.edit_labels)) },
                            onClick = {
                                viewModel.onEvent(TodosEvent.ToggleEditLabelModal)
                                viewModel.onEvent(TodosEvent.ToggleMenuDropdown)
                            },
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(R.drawable.baseline_label_24),
                                    contentDescription = "Label menu icon"
                                )
                            },
                            trailingIcon = {
                                Icon(Icons.Default.Edit, contentDescription = "Edit labels")
                            })
                        DropdownMenuItem(text = { Text(stringResource(R.string.sort_by)) },
                            onClick = {
                                viewModel.onEvent(TodosEvent.ToggleSortDropdown)
                                viewModel.onEvent(TodosEvent.ToggleMenuDropdown)
                            },
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(R.drawable.baseline_sort_24),
                                    contentDescription = "Sort menu icon"
                                )
                            },
                            trailingIcon = {
                                Icon(Icons.Default.ArrowDropDown, contentDescription = "")
                            })
                    }
                    DropdownMenu(expanded = state.isSortDropdownOpen, onDismissRequest = {
                        viewModel.onEvent(TodosEvent.ToggleSortDropdown)
                    }) {
                        DropdownMenuItem(text = { Text("Title") }, onClick = {
                            viewModel.onEvent(TodosEvent.ChangeOrderOption(OrderOptions.TITLE))
                        }, trailingIcon = {
                            if (state.todoOrderOptionPreference == OrderOptions.TITLE) {
                                if (state.todoOrderDirectionPreference == OrderDirection.DESCENDING) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.baseline_arrow_downward_24),
                                        contentDescription = stringResource(R.string.descending_sort_icon)
                                    )
                                } else {
                                    Icon(
                                        painter = painterResource(id = R.drawable.baseline_arrow_upward_24),
                                        contentDescription = stringResource(R.string.ascending_sort_icon)
                                    )
                                }
                            }
                        })
                        DropdownMenuItem(text = { Text("Date") }, onClick = {
                            viewModel.onEvent(TodosEvent.ChangeOrderOption(OrderOptions.DATE))

                        }, trailingIcon = {
                            if (state.todoOrderOptionPreference == OrderOptions.DATE) {
                                if (state.todoOrderDirectionPreference == OrderDirection.DESCENDING) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.baseline_arrow_downward_24),
                                        contentDescription = stringResource(R.string.descending_sort_icon)
                                    )
                                } else {
                                    Icon(
                                        painter = painterResource(id = R.drawable.baseline_arrow_upward_24),
                                        contentDescription = stringResource(R.string.ascending_sort_icon)
                                    )
                                }
                            }
                        })
                        DropdownMenuItem(text = { Text("Priority") }, onClick = {
                            viewModel.onEvent(TodosEvent.ChangeOrderOption(OrderOptions.PRIORITY))

                        }, trailingIcon = {
                            if (state.todoOrderOptionPreference == OrderOptions.PRIORITY) {
                                if (state.todoOrderDirectionPreference == OrderDirection.DESCENDING) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.baseline_arrow_downward_24),
                                        contentDescription = stringResource(R.string.descending_sort_icon)
                                    )
                                } else {
                                    Icon(
                                        painter = painterResource(id = R.drawable.baseline_arrow_upward_24),
                                        contentDescription = stringResource(R.string.ascending_sort_icon)
                                    )
                                }
                            }
                        })
                        DropdownMenuItem(text = { Text("Label") }, onClick = {
                            viewModel.onEvent(TodosEvent.ChangeOrderOption(OrderOptions.LABEL))

                        }, trailingIcon = {
                            if (state.todoOrderOptionPreference == OrderOptions.LABEL) {
                                if (state.todoOrderDirectionPreference == OrderDirection.DESCENDING) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.baseline_arrow_downward_24),
                                        contentDescription = stringResource(R.string.descending_sort_icon)
                                    )
                                } else {
                                    Icon(
                                        painter = painterResource(id = R.drawable.baseline_arrow_upward_24),
                                        contentDescription = stringResource(R.string.ascending_sort_icon)
                                    )
                                }
                            }
                        })
                    }
                }
            })
    }, snackbarHost = { SnackbarHost(snackbarHostState) }, floatingActionButton = {
        FloatingActionButton(onClick = {
            viewModel.onEvent(TodosEvent.ToggleAddEditBottomSheetModal(null))
        }, containerColor = MaterialTheme.colorScheme.primary) {
            Icon(Icons.Default.Add, contentDescription = "Add todo")
        }
    }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            var selectedTabIndex by remember { mutableIntStateOf(TodoSections.INBOX.indexPos) }
            val pagerState = rememberPagerState {
                TodoSections.entries.size
            }

            LaunchedEffect(selectedTabIndex) {
                pagerState.animateScrollToPage(selectedTabIndex)
                scope.launch {
                    viewModel.onEvent(TodosEvent.ToggleSection(TodoSections.entries.find { it.indexPos == selectedTabIndex }!!))
                }
            }
            LaunchedEffect(
                pagerState.currentPage,
//                pagerState.isScrollInProgress
            ) {
                // Uncomment if more tabs are needed. Will fix getting stuck on 2nd tab if navigating
                // from tab 1 to tab 3 etc
//                if (!pagerState.isScrollInProgress) {
                selectedTabIndex = pagerState.currentPage
//                }
            }

            TabRow(modifier = Modifier.padding(horizontal = 16.dp),
                selectedTabIndex = selectedTabIndex,
                indicator = { tabPositions ->
                    Box(
                        Modifier
                            // you need implementation("com.google.accompanist:accompanist-pager-indicators:0.32.0") in build gradle
                            .tabIndicatorOffset(tabPositions[selectedTabIndex])
                            .fillMaxHeight()
                            .padding(horizontal = 30.dp, vertical = 5.dp)
                            .clip(RoundedCornerShape(54.dp))
                            .background(color = MaterialTheme.colorScheme.tertiaryContainer)
                    )
                },
                divider = {

                }) {

                TodoSections.entries.forEach() { section ->
                    val animatedColor by animateColorAsState(
                        if (currentSection == section) MaterialTheme.colorScheme.onTertiaryContainer else MaterialTheme.colorScheme.onSurface,
                        label = "color",
                        animationSpec = tween(
                            durationMillis = 150,
                            easing = FastOutSlowInEasing
                        )
                    )

                    Tab(modifier = Modifier.zIndex(1f),
                        selected = currentSection == section,
                        onClick = {
                            selectedTabIndex = section.indexPos
                            scope.launch {
                                pagerState.animateScrollToPage(section.indexPos)
                            }
                            viewModel.onEvent(TodosEvent.ToggleSection(section))
                        }) {
                        Text(
                            section.displayString,
                            style = MaterialTheme.typography.titleMedium,
                            color = animatedColor,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }
            }

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp, horizontal = 16.dp)
            )

            HorizontalPager(
                state = pagerState, modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                when (it) {
                    TodoSections.INBOX.indexPos -> {
                        TodoInboxSection(
                            state.todos,
                            snackbarHostState,
                            { todo -> viewModel.onEvent(TodosEvent.ToggleCompleted(todo)) },
                            { todo -> viewModel.onEvent(TodosEvent.DeleteTodo(todo)) },
                            { viewModel.onEvent(TodosEvent.RestoreTodo) },
                            { todoId ->
                                viewModel.onEvent(
                                    TodosEvent.ToggleAddEditBottomSheetModal(
                                        todoId
                                    )
                                )
                            },
                            { newDueDate -> viewModel.onEvent(TodosEvent.RescheduleTodos(newDueDate)) },
                            state.isDatePickerVisible,
                            { viewModel.onEvent(TodosEvent.ToggleDatePickerDialog) },
                            scope
                        )
                    }

                    TodoSections.COMPLETED.indexPos -> {
                        TodoIsCompletedSection(
                            state.todos,
                            snackbarHostState,
                            { todo -> viewModel.onEvent(TodosEvent.ToggleCompleted(todo)) },
                            { todo -> viewModel.onEvent(TodosEvent.DeleteTodo(todo)) },
                            { viewModel.onEvent(TodosEvent.RestoreTodo) },
                            { todoId ->
                                viewModel.onEvent(
                                    TodosEvent.ToggleAddEditBottomSheetModal(
                                        todoId
                                    )
                                )
                            },
                            scope
                        )

                    }
                }
            }
        }
    }

    if (state.isAddEditBottomSheetModalVisible) {
        AddEditBottomSheetModal(
            modifier = Modifier, onDismissRequest = {
                viewModel.onEvent(
                    TodosEvent.ToggleAddEditBottomSheetModal(
                        state.currentTodoId
                    )
                )
            }, todoId = state.currentTodoId
        )
    }

    if (state.isEditLabelsModalVisible) {
        ConfigureLabelModal(labelList = state.labelsList,
            addLabel = { value ->
                viewModel.onEvent(TodosEvent.AddLabel(value))
            },
            editLabel = { value ->
                viewModel.onEvent(TodosEvent.UpdateLabel(value))
            },
            deleteLabel = { value ->
                viewModel.onEvent(TodosEvent.UpdateSelectedLabelToDelete(value))
                viewModel.onEvent(TodosEvent.ToggleDeleteLabelDialog)
            },

            onDismissRequest = { viewModel.onEvent(TodosEvent.ToggleEditLabelModal) })


        if (state.isDeleteLabelConfirmationVisible) {
            DeleteLabelConfirmationDialog(
                onDeleteConfirm = {
                    viewModel.onEvent(TodosEvent.DeleteSelectedLabel)
                    viewModel.onEvent(TodosEvent.ToggleDeleteLabelDialog)
                },
                onDeleteCancel = { viewModel.onEvent(TodosEvent.ToggleDeleteLabelDialog) },
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}