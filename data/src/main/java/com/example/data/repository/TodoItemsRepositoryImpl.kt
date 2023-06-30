package com.example.data.repository

import android.app.Application
import android.util.Log
import com.example.data.database.ToDoDao
import com.example.data.mapper.Mapper
import com.example.data.network.ApiService
import com.example.data.network.RequestResult
import com.example.data.network.models.ElementDto
import com.example.data.network.models.ToDoItemDto
import com.example.data.network.result
import com.example.data.core.preferences.RevisionPreference
import com.example.data.mapper.DtoDbMapper
import com.example.domain.entity.TodoItemEntity
import com.example.domain.entity.remote.Result
import com.example.domain.repository.TodoItemsRepository
import java.net.UnknownHostException
import javax.inject.Inject

class TodoItemsRepositoryImpl @Inject constructor(
    private val toDoDao: ToDoDao,
    private val mapper: Mapper,
    private val dbDtoMapper: DtoDbMapper,
    private val apiService: ApiService,
    private val revisionPreference: RevisionPreference
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

    override suspend fun loadData(): Result {
        try {
            val listFromServer = apiService.getToDoList()
            listFromServer.body()?.let {
                updateRevision(it.revision)
            }
            when (listFromServer.result()) {
                is RequestResult.SUCCESS -> {
                    when (listFromServer.code()) {
                        401 -> return Result.AUTH_ERROR
                        500 -> return Result.SERVER_ERROR
                    }
                }
                is RequestResult.ERROR -> {}
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
        logMessage(dtoList.toString())
        try {
            val requestUpdate = apiService.updateToDoList(revisionPreference.getRevision(), dtoList)
            requestUpdate.body()?.let { updateRevision(it.revision) }
            when (requestUpdate.result()) {
                is RequestResult.ERROR -> logMessage("Server updated error")
                is RequestResult.SUCCESS -> logMessage("Server updated successfully")
            }
        } catch (e: Exception) {
            e.stackTrace
        }
    }

    private fun updateRevision(newRevision: Int) {
        revisionPreference.setRevision(newRevision)
    }

    private fun logMessage(msg: String) {
        Log.d(TAG, msg)
    }

    companion object {
        const val TAG = "zyzz"
    }

}