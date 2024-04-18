package com.example.tickerapp.feature_todo.presentation.todos.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.tickerapp.feature_todo.presentation.util.TodoSections

@Composable
fun SectionsMenuBar(
    currentSection: TodoSections,
    modifier: Modifier,
    toggleSection: (TodoSections) -> Unit,
) {

    Row(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
            SectionButton(TodoSections.INBOX, currentSection) {
                toggleSection(TodoSections.INBOX)
            }
        }
        Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
            SectionButton(TodoSections.COMPLETED, currentSection) {
                toggleSection(TodoSections.COMPLETED)
            }
        }
    }

}