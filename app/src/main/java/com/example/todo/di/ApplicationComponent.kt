package com.example.todo.di

import android.app.Application
import com.example.data.di.ApplicationScope
import com.example.data.di.DataModule
import com.example.data.di.WorkerModule
import com.example.presentation.di.PresentationComponent
import com.example.presentation.di.PresentationModule
import com.example.todo.app.ToDoApp
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(
    modules = [
        DataModule::class,
        WorkerModule::class,
        PresentationModule::class
    ]
)
interface ApplicationComponent {
    fun inject(application: ToDoApp)

    @Component.Factory
    interface ApplicationComponentFactory {
        fun create(
            @BindsInstance application: Application
        ): ApplicationComponent
    }

    fun presentationComponentFactory(): PresentationComponent.Factory
}
