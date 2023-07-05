package com.example.todo.app

import android.app.Application
import androidx.work.Configuration
import com.example.data.workers.WorkerFactory
import com.example.presentation.di.PresentationComponent
import com.example.presentation.di.PresentationComponentProvider
import com.example.todo.di.DaggerApplicationComponent
import com.yandex.authsdk.YandexAuthLoginOptions
import com.yandex.authsdk.YandexAuthOptions
import com.yandex.authsdk.YandexAuthSdk
import javax.inject.Inject

class ToDoApp: Application(), Configuration.Provider, PresentationComponentProvider  {
    
    @Inject
    lateinit var workerFactory: WorkerFactory

    val component by lazy {
        DaggerApplicationComponent.factory().create(this)
    }


    override fun onCreate() {
        component.inject(this)
        super.onCreate()
    }

    private val sdk by lazy {
        YandexAuthSdk(this, YandexAuthOptions( this,  true))
    }

    private val loggingOptionsBuilder by lazy {
        YandexAuthLoginOptions.Builder()
    }

    private val authIntent by lazy {
        sdk.createLoginIntent(loggingOptionsBuilder.build())
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