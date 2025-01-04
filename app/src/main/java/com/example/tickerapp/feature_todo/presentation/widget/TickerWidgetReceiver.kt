package com.example.tickerapp.feature_todo.presentation.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.appwidget.updateAll
import androidx.glance.state.PreferencesGlanceStateDefinition
import com.example.tickerapp.feature_todo.domain.model.Entities.Todo
import com.example.tickerapp.feature_todo.domain.model.OrderOptions
import com.example.tickerapp.feature_todo.domain.use_case.Todos.TodoUseCases
import com.example.tickerapp.feature_todo.domain.util.OrderDirection
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TickerWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget
        get() = TickerWidget

    private val coroutineScope = MainScope()

    @Inject
    lateinit var todoUseCases: TodoUseCases

    private fun observeData(context: Context) {

        coroutineScope.launch {
            todoUseCases.getTodosWithLabel(OrderDirection.ASCENDING, OrderOptions.DATE)
                .collectLatest { allTodos ->
                    val serializedTodos = Gson().toJson(allTodos)
                    Log.d("TickerWidgetReceiver", "serializedTodos $serializedTodos")

                    val glanceId =
                        GlanceAppWidgetManager(context).getGlanceIds(TickerWidget::class.java)
                            .firstOrNull()
                    Log.d("TickerWidgetReceiver", "glanceId from observeData - ${glanceId}")

                    val glanceIds =
                        GlanceAppWidgetManager(context).getGlanceIds(TickerWidget::class.java)


                    if (glanceId != null) {
                        glanceIds.forEach { id ->
                            updateAppWidgetState(
                                context,
                                PreferencesGlanceStateDefinition,
                                id
                            ) { prefs ->
                                prefs.toMutablePreferences().apply {
                                    this[allTodosKey] = serializedTodos
                                    this[toggledTodoKey] = "default_value"
                                }
                            }
                        }
//                    glanceAppWidget.update(context, glanceId)
                        glanceAppWidget.updateAll(context)
                    }
                }
        }
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        Log.d("TickerWidgetReceiver", "onUpdate Triggered")
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        observeData(context)

    }

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("TickerWidgetReceiver", "onReceive Triggered")
        Log.d("TickerWidgetReceiver", "intent action - ${intent.action}")
        super.onReceive(context, intent)

        when (intent.action) {
            TOGGLE_CHECKBOX_ACTION -> {
                Log.d("TickerWidgetReceiver", "TOGGLE_ACTION intent received")
                val todoAsString = intent.getStringExtra("toggledTodo")

                Log.d("TickerWidgetReceiver", "$todoAsString")

                coroutineScope.launch {
                    val glanceId =
                        GlanceAppWidgetManager(context).getGlanceIds(TickerWidget::class.java)
                            .firstOrNull()
                    Log.d("TickerWidgetReceiver", "glanceId from onReceive - ${glanceId}")
                    todoUseCases.toggleCompleted(
                        Gson().fromJson<Todo>(
                            todoAsString,
                            object : TypeToken<Todo>() {}.type
                        )
                    )
                    observeData(context)
                }
            }

            REFRESH_WIDGET_ACTION -> {
                Log.d("TickerWidgetReceiver", "REFRESH_WIDGET_ACTION received")

                observeData(context)
            }

            else -> {
                observeData(context)

            }
        }
    }

    companion object {
        val allTodosKey = stringPreferencesKey("all_todos")
        val toggledTodoKey = stringPreferencesKey("toggled_todo")
        const val REFRESH_WIDGET_ACTION = "refreshWidget"
        const val TOGGLE_CHECKBOX_ACTION = "toggleAction"

    }
}
