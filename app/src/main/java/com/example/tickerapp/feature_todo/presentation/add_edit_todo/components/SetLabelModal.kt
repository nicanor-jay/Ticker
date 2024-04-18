package com.example.tickerapp.feature_todo.presentation.add_edit_todo.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.tickerapp.R
import com.example.tickerapp.common.utils.Constants
import com.example.tickerapp.feature_todo.domain.model.Entities.Label

@Composable
fun LabelModal(
    modifier: Modifier = Modifier,
    labelList: List<Label>,
    selectedIndex: Int,
    updateSelectedIndex: (Int) -> Unit,
    addNewLabel: (Label) -> Unit,
    onDismissRequest: () -> Unit,

    ) {

    var isEditing by remember { mutableStateOf(false) }
    var newLabel by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismissRequest) {
        ElevatedCard(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {

                Text(
                    "Set Label",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = modifier.align(Alignment.CenterHorizontally)
                )
                HorizontalDivider(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp), thickness = 1.dp,
                    color = MaterialTheme.colorScheme.secondary
                )

                labelList.forEachIndexed { index, item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .selectable(selected = (index == selectedIndex)) {
                                updateSelectedIndex(index)
                            }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(color = MaterialTheme.colorScheme.secondaryContainer)
                                .padding(8.dp) // Adjust padding as needed
                                .height(40.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_label_24),
                                "Label icon",
                                tint = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Spacer(modifier = modifier.width(4.dp))
                            Text(
                                text = item.labelString,
                                modifier = Modifier.weight(1f),
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                }

//              Add new label
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { isEditing = true }
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
                            unfocusedTrailingIconColor = Color.Transparent
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
                            IconButton(
                                colors = IconButtonDefaults.iconButtonColors(disabledContentColor = Color.Transparent),
                                enabled = newLabel.isNotEmpty(),
                                onClick = {
                                    addNewLabel(Label(labelString = newLabel))
                                    newLabel = ""
                                    isEditing = false
                                }

                            ) {
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