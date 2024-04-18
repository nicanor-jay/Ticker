package com.example.tickerapp.feature_todo.presentation.todos.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.example.tickerapp.feature_todo.presentation.util.TodoSections

@Composable
fun SectionButton(
    section: TodoSections,
    currentSection: TodoSections,
    onClick: () -> Unit,
) {
    val animatedColor by animateColorAsState(
        if (currentSection == section) MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.surface,
        label = "color",
        animationSpec = tween(
            durationMillis = 300, easing = FastOutSlowInEasing
        )
    )

    Button(
        onClick = onClick, colors = ButtonDefaults.buttonColors(containerColor = animatedColor)
    ) {
        Text(
            section.displayString,
            style = MaterialTheme.typography.titleMedium,
            color = if (currentSection == section) MaterialTheme.colorScheme.onTertiaryContainer else MaterialTheme.colorScheme.onSurface
        )

    }
}