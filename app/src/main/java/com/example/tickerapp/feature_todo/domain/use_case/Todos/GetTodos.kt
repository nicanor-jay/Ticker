package com.example.tickerapp.feature_todo.domain.use_case.Todos

import com.example.tickerapp.feature_todo.domain.model.Entities.Todo
import com.example.tickerapp.feature_todo.domain.model.OrderOptions
import com.example.tickerapp.feature_todo.domain.repository.TodoRepository
import com.example.tickerapp.feature_todo.domain.util.OrderDirection
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetTodos(
    private val repository: TodoRepository
) {
    operator fun invoke(
        orderDirection: OrderDirection = OrderDirection.ASCENDING,
        orderOption: OrderOptions = OrderOptions.DATE
    ): Flow<List<Todo>> {
        return repository.getTodos().map { todos ->
            when (orderDirection) {
                OrderDirection.ASCENDING -> {
                    when (orderOption) {
                        OrderOptions.TITLE -> todos.sortedWith(
                            compareBy(
                                { it.isCompleted },
                                { it.title.lowercase() })
                        )

                        OrderOptions.DATE -> todos.sortedWith(
                            compareBy(
                                { it.isCompleted },
                                { it.timeStampDueDate })
                        )

                        OrderOptions.LABEL -> todos.sortedWith(
                            compareBy(
                                { it.isCompleted },
                                { it.labelId })
                        )

                        OrderOptions.PRIORITY -> todos.sortedWith(
                            compareBy(
                                { it.isCompleted },
                                { it.priority.orderNum })
                        )
                    }
                }

                OrderDirection.DESCENDING -> {
                    when (orderOption) {
                        OrderOptions.TITLE -> todos.sortedWith(
                            compareByDescending<Todo> { !it.isCompleted }.thenByDescending { it.title.lowercase() })

                        OrderOptions.DATE -> todos.sortedWith(
                            compareByDescending<Todo> { !it.isCompleted }.thenByDescending { it.timestampCreated })

                        OrderOptions.LABEL -> todos.sortedWith(
                            compareByDescending<Todo> { !it.isCompleted }.thenByDescending { it.labelId })

                        OrderOptions.PRIORITY -> todos.sortedWith(
                            compareByDescending<Todo> { !it.isCompleted }.thenByDescending {
                                it.priority.orderNum
                            })
                    }
                }
            }
        }
    }
}