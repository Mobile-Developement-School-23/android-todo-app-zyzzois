package com.example.data.repository

import android.app.Application
import androidx.work.WorkManager
import com.example.data.core.preferences.RevisionPreference
import com.example.data.database.ToDoDao
import com.example.data.mapper.DtoDbMapper
import com.example.data.mapper.Mapper
import com.example.data.network.RequestResult
import com.example.data.network.RetrofitClient
import com.example.data.network.result
import com.example.data.workers.NetworkWorker
import com.example.domain.entity.TodoItemEntity
import com.example.domain.entity.remote.Result
import com.example.domain.repository.TodoItemsRepository
import java.net.UnknownHostException
import javax.inject.Inject

class TodoItemsRepositoryImpl @Inject constructor(
    private val toDoDao: ToDoDao,
    private val mapper: Mapper,
    private val apiService: RetrofitClient,
    private val dbDtoMapper: DtoDbMapper,
    private val revisionPreference: RevisionPreference,
    private val application: Application
): TodoItemsRepository {

    override suspend fun addNewItem(item: TodoItemEntity) {
        val dbModel = mapper.mapEntityToDbModel(item)
        toDoDao.addTodo(dbModel)
        updateServer()
    }

    override suspend fun getItemById(itemId: Int): TodoItemEntity {
        val dbItem = toDoDao.getItemById(itemId)
        return mapper.mapDbModelToEntity(dbItem)
    }

    override suspend fun editItem(item: TodoItemEntity) {
        toDoDao.addTodo(mapper.mapEntityToDbModel(item))
        updateServer()
    }

    override suspend fun getItemsList() = toDoDao.getToDoList().map {
        mapper.mapDbModelToEntity(it)
    }

    override suspend fun enableBackGroundUpdates() {
        val workManager = WorkManager.getInstance(application)
        workManager.enqueue(NetworkWorker.makeRequest())
    }

    override suspend fun loadData(): Result {
        try {
            val request = apiService.api.getToDoList()
            request.body()?.let {
                updateRevision(it.revision)
            }
            when (request.result()) {
                is RequestResult.SUCCESS -> {
                    toDoDao.clearTable()
                    request.body()?.let {
                        dbDtoMapper.mapListDtoToListModelDb(it)
                    }?.forEach {
                        toDoDao.addTodo(it)
                    }
                    return Result.SUCCESS
                }
                is RequestResult.ERROR -> {
                    when (request.code()) {
                        401 -> return Result.AUTH_ERROR
                        500 -> return Result.SERVER_ERROR
                        400 -> return Result.REVISION_ERROR
                    }
                }
            }

        } catch (e: UnknownHostException) {
            return Result.INTERNET_CONNECTION_ERROR
        } catch (e: Exception) {
            e.stackTrace
        }
        return Result.SUCCESS
    }

    override suspend fun deleteItem(itemId: Int) {
        toDoDao.deleteToDoItem(itemId)
        updateServer()
    }

    private suspend fun updateServer() {
        val dtoList = dbDtoMapper.mapListModelDbToListDto(toDoDao.getToDoList())
        try {
            val currentRevision = revisionPreference.getRevision()
            val requestUpdate = apiService.api.updateToDoList(currentRevision, dtoList)
            requestUpdate.body()?.let {
                toDoDao.updateList(dbDtoMapper.mapListDtoToListModelDb(it))
                updateRevision(it.revision)
            }
        } catch (e: Exception) {
            e.stackTrace
        }
    }

    private fun updateRevision(newRevision: Int) {
        revisionPreference.setRevision(newRevision)
    }

}