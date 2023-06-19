package com.example.presentation.di

import android.app.Application
import com.example.presentation.screens.DetailFragment
import com.example.presentation.screens.ListFragment
import dagger.BindsInstance
import dagger.Component

@Component
interface PresentationComponent {
    fun inject(fragment: ListFragment)
    fun inject(fragment: DetailFragment)

    @Component.Factory
    interface PresentationComponentFactory {
        fun create(
            @BindsInstance application: Application
        ): PresentationComponent
    }
}