package com.example.tickerapp.feature_todo.presentation.widget

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.ColorFilter
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionParametersOf
import androidx.glance.action.clickable
import androidx.glance.appwidget.CheckBox
import androidx.glance.appwidget.CheckboxDefaults
import androidx.glance.appwidget.CircularProgressIndicator
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.itemsIndexed
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.appwidget.updateAll
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.example.tickerapp.R
import com.example.tickerapp.common.utils.Constants
import com.example.tickerapp.feature_todo.domain.model.TodoWithLabel
import com.example.tickerapp.feature_todo.domain.model.getPriorityColor
import com.example.tickerapp.feature_todo.domain.util.formatDate
import com.example.tickerapp.feature_todo.presentation.MainActivity
import com.example.tickerapp.feature_todo.presentation.util.isAfterDay
import com.example.tickerapp.feature_todo.presentation.util.isSameDay
import com.example.tickerapp.feature_todo.presentation.widget.TickerWidgetReceiver.Companion.REFRESH_WIDGET_ACTION
import com.example.tickerapp.feature_todo.presentation.widget.TickerWidgetReceiver.Companion.TOGGLE_CHECKBOX_ACTION
import com.example.tickerapp.ui.theme.Green
import com.example.tickerapp.ui.theme.LogoPurple
import com.example.tickerapp.ui.theme.Red
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object TickerWidget : GlanceAppWidget() {

    override val stateDefinition: GlanceStateDefinition<*> = PreferencesGlanceStateDefinition

    override suspend fun provideGlance(context: Context, id: GlanceId) {

        provideContent {
            GlanceTheme {
                MyWidget()
            }
        }
    }
}

@Composable
private fun MyWidget() {

    val context = LocalContext.current
    val deserializedList = currentState(key = TickerWidgetReceiver.allTodosKey)

    var isRefreshing by remember { mutableStateOf(false) }

    val currentTimeStamp = System.currentTimeMillis()

    Column(
        modifier = GlanceModifier.background(MaterialTheme.colorScheme.surface).fillMaxSize(),
    ) {

        Row(
            modifier = GlanceModifier.fillMaxWidth().padding(end = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    contentDescription = "Logo Icon",
                    provider = ImageProvider(R.drawable.ic_launcher_foreground),
                    colorFilter = ColorFilter.tint(ColorProvider(LogoPurple)),
                    modifier = GlanceModifier.size(45.dp).padding(0.dp)
                        .clickable(
                            onClick = actionStartActivity(
                                Intent(
                                    context,
                                    MainActivity::class.java
                                )
//                                    .setFlags(FLAG_ACTIVITY_SINGLE_TOP)
                            )
                        )
                )
                Text(
                    "Inbox",
                    style = TextStyle(
                        color = ColorProvider(LogoPurple),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    ),
                )
            }
            Spacer(modifier = GlanceModifier.defaultWeight())
            Image(
                contentDescription = "Refresh widget icon button",
                provider = ImageProvider(R.drawable.baseline_refresh_24),
                colorFilter = ColorFilter.tint(ColorProvider(LogoPurple)),
                modifier = GlanceModifier.size(26.dp)
                    .clickable {

                        CoroutineScope(Dispatchers.Main).launch {

                            isRefreshing = true
                            // Broadcast refresh action
                            val refreshIntent =
                                Intent(context, TickerWidgetReceiver::class.java).apply {
                                    action = REFRESH_WIDGET_ACTION
                                }
                            context.sendBroadcast(refreshIntent)
                            // Delay for 2 seconds and then set isRefreshing back to false
                            delay(500) // Adjust the delay time as needed
                            isRefreshing = false
                        }
                    }
            )
            Spacer(modifier = GlanceModifier.width(6.dp))
            Image(
                contentDescription = "Open app icon button",
                provider = ImageProvider(R.drawable.baseline_add_24),
                colorFilter = ColorFilter.tint(ColorProvider(LogoPurple)),
                modifier = GlanceModifier.size(26.dp)
                    .clickable(
                        onClick = actionStartActivity(
                            Intent(
                                context,
                                MainActivity::class.java
                            )
//                                .setFlags(FLAG_ACTIVITY_SINGLE_TOP)
                                .apply {
                                    putExtra("isAddingNewTodoIntent", true)
                                }
                        )
                    )
            )
        }

        if (isRefreshing) {
            CircularProgressIndicator(
                modifier = GlanceModifier.fillMaxWidth(), color = ColorProvider(
                    LogoPurple
                )
            )
            return@Column
        }

        if (deserializedList == null) {
            return@Column
        }

        val todoList = Gson().fromJson<List<TodoWithLabel>>(
            deserializedList, object : TypeToken<List<TodoWithLabel>>() {}.type
        )

        if (todoList.none { !it.todo.isCompleted }) {
            Column(modifier = GlanceModifier.fillMaxWidth().padding(horizontal = 8.dp)) {
                Text("No todos active")
            }
            return@Column
        }


        val reorganisedTodoList = todoList.filter { !it.todo.isCompleted }
//            .sortedBy { it.todo.timeStampDueDate }

        LazyColumn(modifier = GlanceModifier.fillMaxSize()) {
            itemsIndexed(reorganisedTodoList) { index, todoWithLabel ->
                val checkboxColors = CheckboxDefaults.colors(
                    uncheckedColor = ColorProvider(getPriorityColor(todoWithLabel.todo.priority)),
                    checkedColor = ColorProvider(getPriorityColor(todoWithLabel.todo.priority))
                )

                val dateColor =
                    if (isSameDay(currentTimeStamp, todoWithLabel.todo.timeStampDueDate)) {
                        ColorProvider(Green)
                    } else if (isAfterDay(
                            todoWithLabel.todo.timeStampDueDate,
                            currentTimeStamp
                        )
                    ) {
                        ColorProvider(Color.Black)
                    } else {
                        ColorProvider(Red)
                    }

                val dateText =
                    if (isSameDay(currentTimeStamp, todoWithLabel.todo.timeStampDueDate)) {
                        "Today"
                    } else {
                        formatDate(todoWithLabel.todo.timeStampDueDate, false)
                    }

                Column(modifier = GlanceModifier.fillMaxWidth().padding(horizontal = 8.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = GlanceModifier.padding(vertical = 2.dp)
                    ) {
                        CheckBox(colors = checkboxColors,
                            checked = todoWithLabel.todo.isCompleted,
                            onCheckedChange = {
                                val updatedList = todoList.map {
                                    if (it.todo.id == todoWithLabel.todo.id) {
                                        it.copy(todo = it.todo.copy(isCompleted = !it.todo.isCompleted))
                                    } else {
                                        it
                                    }
                                }
                                Log.d("TickerWidget", "updatedList - ${Gson().toJson(updatedList)}")
                                actionRunCallback<ToggleCheckboxActionCallback>(
                                    parameters = actionParametersOf(
                                        ActionParameters.Key<String>("UPDATED_LIST") to Gson().toJson(
                                            updatedList
                                        ),
                                    )
                                )

                                val intent =
                                    Intent(context, TickerWidgetReceiver::class.java).apply {
                                        action = TOGGLE_CHECKBOX_ACTION
                                    }.putExtra(
                                        "toggledTodo", Gson().toJson(
                                            todoWithLabel.todo
                                        )
                                    )
                                context.sendBroadcast(intent)

                            })
                        Spacer(modifier = GlanceModifier.width(4.dp))
                        Column {
                            Text(
                                text = todoWithLabel.todo.title,
                                style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium)
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Image(
                                    contentDescription = "Calendar Icon",
                                    provider = ImageProvider(R.drawable.baseline_calendar_month_24),
                                    colorFilter = ColorFilter.tint(dateColor),
                                    modifier = GlanceModifier.size(14.dp).padding(0.dp)
                                )
                                Spacer(modifier = GlanceModifier.width(2.dp))
                                Text(
                                    text = dateText,
                                    style = TextStyle(
                                        fontSize = 12.sp,
                                        color = dateColor,
                                    )
                                )
                                if (todoWithLabel.label != Constants.DEFAULT_LABEL) {
                                    Spacer(modifier = GlanceModifier.width(8.dp))
                                }
                                if (todoWithLabel.label != Constants.DEFAULT_LABEL) {
                                    Image(
                                        contentDescription = "Label Icon",
                                        provider = ImageProvider(R.drawable.baseline_label_24),
                                        modifier = GlanceModifier.size(14.dp).padding(0.dp)
                                    )
                                    Spacer(modifier = GlanceModifier.width(2.dp))
                                    Text(
                                        text = todoWithLabel.label.labelString,
                                        style = TextStyle(
                                            fontSize = 12.sp,
                                        ),
                                    )
                                }
                            }
                        }
                    }
                    if (index != reorganisedTodoList.lastIndex) {
                        Spacer(
                            modifier = GlanceModifier
                                .height(1.dp)  // Adjust height for desired divider thickness
                                .background(Color.LightGray)
                                .fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

object ToggleCheckboxActionCallback : ActionCallback {
    override suspend fun onAction(
        context: Context, glanceId: GlanceId, parameters: ActionParameters
    ) {
        updateAppWidgetState(context, glanceId) { prefs ->
            prefs.toMutablePreferences().apply {

                // Assign the value for updated list key
                prefs[TickerWidgetReceiver.allTodosKey] =
                    parameters[ActionParameters.Key("UPDATED_LIST")].toString()

            }
        }

        TickerWidget.updateAll(context)
//        TickerWidget.update(context, glanceId)
    }
}