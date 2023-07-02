package com.example.todo.di

import android.app.Application
import com.example.todo.app.ToDoApp
import com.example.todo.ui.screens.auth.AuthActivity
import com.example.todo.ui.screens.detail.DetailFragment
import com.example.todo.ui.screens.main.ListFragment
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(
    modules = [
       DataModule::class,
       ViewModelModule::class,
       WorkerModule::class
    ]
)
interface ApplicationComponent {
    fun inject(fragment: ListFragment)
    fun inject(fragment: DetailFragment)
    fun inject(application: ToDoApp)
    fun inject(activity: AuthActivity)

    @Component.Factory
    interface ApplicationComponentFactory {
        fun create(
            @BindsInstance application: Application
        ): ApplicationComponent
    }

}