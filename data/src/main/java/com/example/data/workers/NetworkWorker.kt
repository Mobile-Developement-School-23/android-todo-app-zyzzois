package com.example.data.workers

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkerParameters
import com.example.data.core.preferences.RevisionPreference
import com.example.data.database.ToDoDao
import com.example.data.mapper.DtoDbMapper
import com.example.data.network.RequestResult
import com.example.data.network.RetrofitClient
import com.example.data.network.result
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class NetworkWorker (
    context: Context,
    workerParameters: WorkerParameters,
    private val toDoDao: ToDoDao,
    private val mapper: DtoDbMapper,
    private val retrofitClient: RetrofitClient,
    private val revisionPreference: RevisionPreference
): CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        try {
            val request = retrofitClient.api.getToDoList()
            request.body()?.let {
                revisionPreference.setRevision(it.revision)
            }
            when (request.result()) {
                is RequestResult.SUCCESS -> {
                    toDoDao.clearTable()
                    request.body()?.let { mapper.mapListDtoToListModelDb(it) }?.forEach {
                        toDoDao.addTodo(it)
                    }
                }
                is RequestResult.ERROR -> {

                }
            }
        } catch (e: Exception) {
            e.stackTrace
        }
        return Result.success()
    }


    class Factory @Inject constructor(
        private val toDoDao: ToDoDao,
        private val mapper: DtoDbMapper,
        private val retrofitClient: RetrofitClient,
        private val revisionPreference: RevisionPreference
    ): ChildWorkerFactory {
        override fun create(
            context: Context,
            workerParameters: WorkerParameters
        ): ListenableWorker {
            return NetworkWorker(
                context,
                workerParameters,
                toDoDao,
                mapper,
                retrofitClient,
                revisionPreference
            )
        }
    }

    companion object {
        private val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        const val NAME = "NetworkWorker"

        fun makeRequest() = PeriodicWorkRequest.Builder(
            NetworkWorker::class.java,
        8, TimeUnit.HOURS
        ).setConstraints(constraints)
            .build()
    }

}