package com.example.tickerapp.feature_todo.domain.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.example.tickerapp.ui.theme.Green
import com.example.tickerapp.ui.theme.Orange
import com.example.tickerapp.ui.theme.Red

enum class Priority(val displayString: String, val orderNum: Int) {
    DEFAULT("Default", 0),
    LOW("Low", 1),
    MEDIUM("Medium", 2),
    HIGH("High", 3)
}

@Composable
fun getPriorityColor(priority: Priority): Color {
    return when (priority) {
//        Priority.DEFAULT -> CheckboxDefaults.colors().checkedBoxColor.toArgb()
        Priority.DEFAULT -> Color.Gray
        Priority.LOW -> Green
        Priority.MEDIUM -> Orange
        Priority.HIGH -> Red
    }
}
