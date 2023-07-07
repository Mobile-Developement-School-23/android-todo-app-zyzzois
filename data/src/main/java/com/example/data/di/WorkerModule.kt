package com.example.data.di

import com.example.data.workers.ChildWorkerFactory
import com.example.data.workers.NetworkWorker
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface WorkerModule {

    @Binds
    @IntoMap
    @WorkerKey(NetworkWorker::class)
    fun bindNetworkWorkerFactory(worker: NetworkWorker.Factory): ChildWorkerFactory

}