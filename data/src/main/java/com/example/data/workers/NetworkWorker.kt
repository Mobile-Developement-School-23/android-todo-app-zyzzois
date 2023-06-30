package com.example.data.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkerParameters
import com.example.data.core.preferences.RevisionPreference
import com.example.data.database.ToDoDao
import com.example.data.mapper.Mapper
import com.example.data.network.ApiService
import com.example.data.network.RequestResult
import com.example.data.network.result
import com.example.data.repository.TodoItemsRepositoryImpl.Companion.TAG
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class NetworkWorker (
    context: Context,
    workerParameters: WorkerParameters,
    private val toDoDao: ToDoDao,
    private val mapper: Mapper,
    private val apiService: ApiService,
    private val revisionPreference: RevisionPreference
): CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
//        val dtoList = mapper.mapListModelDbToListDto(toDoDao.getToDoList())
//        try {
//            val requestUpdate = apiService.updateToDoList(revisionPreference.getRevision(), dtoList)
//            when (requestUpdate.result()) {
//                is RequestResult.ERROR -> logMessage("Server updated error")
//                is RequestResult.SUCCESS -> logMessage("Server updated successfully")
//            }
//        } catch (e: Exception) {
//            e.stackTrace
//        }
        return Result.success()
    }

    private fun logMessage(msg: String) {
        Log.d(TAG, msg)
    }

    class Factory @Inject constructor(
        private val toDoDao: ToDoDao,
        private val mapper: Mapper,
        private val apiService: ApiService,
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
                apiService,
                revisionPreference
            )
        }
    }

    companion object {
        const val NAME = "NetworkWorker"

        fun makeRequest() = PeriodicWorkRequest.Builder(
            NetworkWorker::class.java,
            8,
            TimeUnit.HOURS
        ).build()
    }

}