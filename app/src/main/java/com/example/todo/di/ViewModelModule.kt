package com.example.todo.di

import androidx.lifecycle.ViewModel
import com.example.todo.ui.screens.detail.DetailViewModel
import com.example.todo.ui.screens.main.ListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @IntoMap
    @Binds
    @ViewModelKey(ListViewModel::class)
    fun bindListViewModel(viewModel: ListViewModel): ViewModel

    @IntoMap
    @Binds
    @ViewModelKey(DetailViewModel::class)
    fun bindDetailViewModel(viewModel: DetailViewModel): ViewModel


}