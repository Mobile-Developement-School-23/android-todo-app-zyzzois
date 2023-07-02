package com.example.todo.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.data.core.preferences.RevisionPreference
import com.example.data.core.preferences.RevisionPreferenceImpl
import com.example.data.database.AppDatabase
import com.example.data.database.ToDoDao
import com.example.data.network.Interceptor
import com.example.data.network.RetrofitClient
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

    @ApplicationScope
    @Binds
    fun provideSharedPreferences(impl: RevisionPreferenceImpl): RevisionPreference

    companion object {
        private const val AUTH_TABLE_NAME = "auth"

        @ApplicationScope
        @Provides
        fun provideToDoDao(
            application: Application
        ): ToDoDao {
            return AppDatabase.getInstance(application).todoDao()
        }

        @Provides
        fun provideSharedPreferences(context: Application): SharedPreferences =
            context.getSharedPreferences(AUTH_TABLE_NAME, Context.MODE_PRIVATE)

        @Provides
        fun provideServiceInterceptor(sharedPreferences: SharedPreferences): Interceptor =
            Interceptor(sharedPreferences)

        @Provides
        fun provideRetrofitClient(interceptor: Interceptor): RetrofitClient =
            RetrofitClient(interceptor)

    }
}