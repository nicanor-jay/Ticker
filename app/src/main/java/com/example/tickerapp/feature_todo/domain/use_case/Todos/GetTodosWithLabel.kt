package com.example.tickerapp.feature_todo.domain.use_case.Todos

import com.example.tickerapp.feature_todo.domain.model.OrderOptions
import com.example.tickerapp.feature_todo.domain.model.TodoWithLabel
import com.example.tickerapp.feature_todo.domain.repository.TodoRepository
import com.example.tickerapp.feature_todo.domain.util.OrderDirection
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetTodosWithLabel(private val repository: TodoRepository) {
    operator fun invoke(
        orderDirection: OrderDirection = OrderDirection.ASCENDING,
        orderOption: OrderOptions = OrderOptions.DATE
    ): Flow<List<TodoWithLabel>> {
        return repository.getTodosWithLabel().map { todos ->
            when (orderDirection) {
                OrderDirection.ASCENDING -> {
                    when (orderOption) {
                        OrderOptions.TITLE -> todos.sortedWith(
                            compareBy(
                                { it.todo.isCompleted },
                                { it.todo.title.lowercase() })
                        )

                        OrderOptions.DATE -> todos.sortedWith(
                            compareBy(
                                { it.todo.isCompleted },
                                { it.todo.timeStampDueDate })
                        )

                        OrderOptions.LABEL -> todos.sortedWith(
                            compareBy(
                                { it.todo.isCompleted },
                                { it.todo.labelId })
                        )

                        OrderOptions.PRIORITY -> todos.sortedWith(
                            compareBy(
                                { it.todo.isCompleted },
                                { it.todo.priority.orderNum })
                        )
                    }
                }

                OrderDirection.DESCENDING -> {
                    when (orderOption) {
                        OrderOptions.TITLE -> todos.sortedWith(
                            compareByDescending<TodoWithLabel> { !it.todo.isCompleted }.thenByDescending { it.todo.title.lowercase() })

                        OrderOptions.DATE -> todos.sortedWith(
                            compareByDescending<TodoWithLabel> { !it.todo.isCompleted }.thenByDescending { it.todo.timestampCreated })

                        OrderOptions.LABEL -> todos.sortedWith(
                            compareByDescending<TodoWithLabel> { !it.todo.isCompleted }.thenByDescending { it.todo.labelId })

                        OrderOptions.PRIORITY -> todos.sortedWith(
                            compareByDescending<TodoWithLabel> { !it.todo.isCompleted }.thenByDescending {
                                it.todo.priority.orderNum
                            })
                    }
                }
            }
        }
    }

}