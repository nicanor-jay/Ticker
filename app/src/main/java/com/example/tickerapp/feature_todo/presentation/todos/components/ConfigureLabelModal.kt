package com.example.tickerapp.feature_todo.presentation.todos.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.tickerapp.R
import com.example.tickerapp.common.utils.Constants
import com.example.tickerapp.feature_todo.domain.model.Entities.Label
import com.example.tickerapp.ui.theme.TickerAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfigureLabelModal(
    modifier: Modifier = Modifier,
    labelList: List<Label>,
    addLabel: (Label) -> Unit,
    editLabel: (Label) -> Unit,
    deleteLabel: (Label) -> Unit,
    onDismissRequest: () -> Unit,
) {
    var selectedIndex by remember { mutableStateOf(-1) }

    var newLabel by remember { mutableStateOf("") }

    val editTextFieldValue = remember {
        mutableStateOf(
            TextFieldValue(
                "",
                TextRange("".length)
            )
        )
    }
    val isEditing = remember { mutableStateOf(false) }

    val focusRequester = remember { FocusRequester() }
    val scrollState = rememberScrollState()

    Dialog(onDismissRequest = onDismissRequest) {
        ElevatedCard(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    "Edit Labels",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = modifier.align(Alignment.CenterHorizontally)
                )

                HorizontalDivider(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.secondary
                )

                Column(
                    modifier = Modifier.verticalScroll(scrollState)
                ) {
                    labelList.forEachIndexed { index, item ->
                        if (item.id == 1) {
                            return@forEachIndexed
                        }
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(color = MaterialTheme.colorScheme.secondaryContainer)
                                    .padding(8.dp) // Adjust padding as needed
                                    .height(40.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {

                                IconButton(
                                    onClick = {
                                        deleteLabel(item)

                                    },
                                ) {
                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = "Delete label icon",
                                    )
                                }

                                if (index == selectedIndex) {
                                    val interactionSource = remember { MutableInteractionSource() }
                                    BasicTextField(
                                        value = editTextFieldValue.value,
                                        onValueChange = {
                                            if (it.text.length < Constants.LABEL_CHARACTER_LIMIT) {
                                                editTextFieldValue.value = it
                                            }
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(TextFieldDefaults.MinHeight)
                                            .padding(0.dp)
                                            .focusRequester(focusRequester),
                                        textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.onSecondaryContainer),
                                        visualTransformation = VisualTransformation.None,
                                        interactionSource = interactionSource,
                                        enabled = true,
                                        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                                        singleLine = true,
                                    ) { innerTextField ->
                                        TextFieldDefaults.DecorationBox(value = editTextFieldValue.value.text,
                                            visualTransformation = VisualTransformation.None,
                                            innerTextField = innerTextField,
                                            singleLine = true,
                                            enabled = true,
                                            interactionSource = interactionSource,
                                            contentPadding = PaddingValues(0.dp), // this is how you can remove the padding
                                            colors = TextFieldDefaults.colors(
                                                focusedIndicatorColor = Color.Transparent,
                                                unfocusedIndicatorColor = Color.Transparent,
                                                focusedContainerColor = Color.Transparent,
                                                unfocusedContainerColor = Color.Transparent
                                            ),
                                            trailingIcon = {
                                                IconButton(
                                                    enabled = editTextFieldValue.value.text.isNotEmpty(),
                                                    onClick = {
                                                        editLabel(
                                                            labelList[selectedIndex].copy(
                                                                labelString = editTextFieldValue.value.text
                                                            )
                                                        )
                                                        selectedIndex = -1
                                                        isEditing.value = false
                                                    }) {
                                                    Icon(
                                                        painter = painterResource(id = R.drawable.baseline_save_24),
                                                        contentDescription = "Save"
                                                    )
                                                }
                                            })
                                    }

                                    LaunchedEffect(Unit) {
                                        focusRequester.requestFocus()
                                    }
                                } else {
                                    Text(
                                        text = item.labelString,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.weight(1f),
                                        color = MaterialTheme.colorScheme.onSecondaryContainer
                                    )
                                    IconButton(
                                        onClick = {
                                            selectedIndex = index

                                            isEditing.value = true
                                            editTextFieldValue.value = TextFieldValue(
                                                item.labelString,
                                                TextRange(item.labelString.length)
                                            )

                                            Log.d(
                                                "ConfigureLabelModal",
                                                "index: $index, labelId: ${labelList[index].id}, labelString: ${labelList[index].labelString}"
                                            )
                                        }, enabled = !isEditing.value
                                    ) {
                                        Icon(
                                            Icons.Default.Edit,
                                            contentDescription = "Edit label icon",
                                            modifier = modifier.padding(0.dp),
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        TextField(
                            value = newLabel,
                            onValueChange = {
                                if (it.length < Constants.LABEL_CHARACTER_LIMIT) {
                                    newLabel = it
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .height(TextFieldDefaults.MinHeight),
                            colors = TextFieldDefaults.colors(
                                unfocusedContainerColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedTrailingIconColor = Color.Transparent,
                            ),
                            shape = RoundedCornerShape(10.dp),
                            placeholder = { Text("Add new label") },
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_new_label_24),
                                    "Add icon",
                                    tint = MaterialTheme.colorScheme.secondary
                                )
                            },
                            trailingIcon = {
                                IconButton(colors = IconButtonDefaults.iconButtonColors(
                                    disabledContentColor = Color.Transparent
                                ),
                                    enabled = newLabel.isNotEmpty(),
                                    onClick = {
                                        addLabel(Label(labelString = newLabel))
                                        newLabel = ""

                                    }) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.baseline_save_24),
                                        contentDescription = "Save"
                                    )
                                }
                            },
                            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun ConfigureLabelModalPreview() {
    TickerAppTheme {
        ConfigureLabelModal(labelList = listOf(
            Constants.DEFAULT_LABEL, Label(2, "Work"), Label(3, "Personal")
        ), addLabel = {}, editLabel = {}, deleteLabel = {}, onDismissRequest = {})
    }
}