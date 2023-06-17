package com.example.todo.di

import android.app.Application
import com.example.data.database.AppDatabase
import com.example.data.database.ToDoDao
import com.example.data.repository.TodoItemsRepositoryImpl
import com.example.domain.repository.TodoItemsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    @ApplicationScope
    @Binds
    fun bindToDoItemsRepository(impl: TodoItemsRepositoryImpl): TodoItemsRepository

    companion object {
        @ApplicationScope
        @Provides
        fun provideToDoDao(
            application: Application
        ): ToDoDao {
            return AppDatabase.getInstance(application).todoDao()
        }
    }

}