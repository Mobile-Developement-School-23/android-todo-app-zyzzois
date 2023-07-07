package com.example.presentation.di

import com.example.presentation.ui.screens.auth.AuthActivity
import com.example.presentation.ui.screens.detail.DetailFragment
import com.example.presentation.ui.screens.main.ListFragment
import dagger.Subcomponent

@ApplicationScope
@Subcomponent(modules = [ViewModelModule::class])
interface PresentationComponent {

    fun inject(fragment: ListFragment)
    fun inject(fragment: DetailFragment)
    fun inject(activity: AuthActivity)

    @Subcomponent.Factory
    interface Factory {
        fun create(): PresentationComponent
    }
}

interface PresentationComponentProvider {
    fun providePresentationComponent(): PresentationComponent.Factory
}
