package com.example.tickerapp.di

import android.app.Application
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.tickerapp.common.utils.Constants
import com.example.tickerapp.feature_todo.data.data_source.TodoDatabase
import com.example.tickerapp.feature_todo.data.repository.DataStoreRepositoryImpl
import com.example.tickerapp.feature_todo.data.repository.LabelRepositoryImpl
import com.example.tickerapp.feature_todo.data.repository.TodoRepositoryImpl
import com.example.tickerapp.feature_todo.domain.repository.DataStoreRepository
import com.example.tickerapp.feature_todo.domain.repository.LabelRepository
import com.example.tickerapp.feature_todo.domain.repository.TodoRepository
import com.example.tickerapp.feature_todo.domain.use_case.Labels.AddLabel
import com.example.tickerapp.feature_todo.domain.use_case.Labels.DeleteLabel
import com.example.tickerapp.feature_todo.domain.use_case.Labels.GetLabels
import com.example.tickerapp.feature_todo.domain.use_case.Labels.LabelUseCases
import com.example.tickerapp.feature_todo.domain.use_case.Labels.UpdateLabel
import com.example.tickerapp.feature_todo.domain.use_case.Preferences.GetBoolean
import com.example.tickerapp.feature_todo.domain.use_case.Preferences.GetString
import com.example.tickerapp.feature_todo.domain.use_case.Preferences.ObserveBoolean
import com.example.tickerapp.feature_todo.domain.use_case.Preferences.ObserveString
import com.example.tickerapp.feature_todo.domain.use_case.Preferences.PreferenceUseCases
import com.example.tickerapp.feature_todo.domain.use_case.Preferences.PutBoolean
import com.example.tickerapp.feature_todo.domain.use_case.Preferences.PutString
import com.example.tickerapp.feature_todo.domain.use_case.Todos.AddTodo
import com.example.tickerapp.feature_todo.domain.use_case.Todos.DeleteTodo
import com.example.tickerapp.feature_todo.domain.use_case.Todos.GetTodo
import com.example.tickerapp.feature_todo.domain.use_case.Todos.GetTodoWithLabel
import com.example.tickerapp.feature_todo.domain.use_case.Todos.GetTodos
import com.example.tickerapp.feature_todo.domain.use_case.Todos.GetTodosWithLabel
import com.example.tickerapp.feature_todo.domain.use_case.Todos.TodoUseCases
import com.example.tickerapp.feature_todo.domain.use_case.Todos.ToggleCompleted
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideTodoDatabase(app: Application): TodoDatabase {
        val databaseCallback = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // Insert initial values into your database tables here
                val labels = listOf(Constants.DEFAULT_LABEL.labelString, "Personal", "Work")
                labels.forEach { label ->
                    val contentValues = ContentValues()
                    contentValues.put("labelString", label)
                    db.insert("labels", SQLiteDatabase.CONFLICT_REPLACE, contentValues)
                }
            }
        }
        return Room.databaseBuilder(
            app,
            TodoDatabase::class.java,
            TodoDatabase.DATABASE_NAME
        )
            .addCallback(databaseCallback)
            .build()
    }

    @Provides
    @Singleton
    fun providesTodoRepository(db: TodoDatabase): TodoRepository {
        return TodoRepositoryImpl(db.todoDao)
    }

    @Provides
    @Singleton
    fun providesLabelRepository(db: TodoDatabase): LabelRepository {
        return LabelRepositoryImpl(db.labelDao)
    }

    @Provides
    @Singleton
    fun provideTodoUseCases(repository: TodoRepository): TodoUseCases {
        return TodoUseCases(
            getTodos = GetTodos(repository),
            getTodo = GetTodo(repository),
            getTodosWithLabel = GetTodosWithLabel(repository),
            getTodoWithLabel = GetTodoWithLabel(repository),
            deleteTodo = DeleteTodo(repository),
            addTodo = AddTodo(repository),
            toggleCompleted = ToggleCompleted(repository)
        )
    }

    @Provides
    @Singleton
    fun provideLabelUseCases(repository: LabelRepository): LabelUseCases {
        return LabelUseCases(
            addLabel = AddLabel(repository),
            updateLabel = UpdateLabel(repository),
            getLabels = GetLabels(repository),
            deleteLabel = DeleteLabel(repository)
        )
    }

    @Provides
    @Singleton
    fun providePreferencesUseCases(repository: DataStoreRepository): PreferenceUseCases {
        return PreferenceUseCases(
            getBoolean = GetBoolean(repository),
            getString = GetString(repository),
            putString = PutString(repository),
            putBoolean = PutBoolean(repository),
            observeString = ObserveString(repository),
            observeBoolean = ObserveBoolean(repository)
        )
    }

    @Singleton
    @Provides
    fun provideDataStoreRepository(
        @ApplicationContext app: Context
    ): DataStoreRepository = DataStoreRepositoryImpl(app)
}