package com.example.todo.app

import android.app.Application
import androidx.work.Configuration
import com.example.data.workers.WorkerFactory
import com.example.presentation.di.PresentationComponent
import com.example.presentation.di.PresentationComponentProvider
import com.example.todo.di.DaggerApplicationComponent
import javax.inject.Inject

class ToDoApp: Application(), Configuration.Provider, PresentationComponentProvider  {
    
    @Inject
    lateinit var workerFactory: WorkerFactory

    private val component by lazy {
        DaggerApplicationComponent.factory().create(this)
    }

    override fun onCreate() {
        component.inject(this)
        super.onCreate()
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
    }

    override fun providePresentationComponent(): PresentationComponent.Factory {
        return component.presentationComponentFactory()
    }

}