package com.example.data.repository

import android.app.Application
import android.util.Log
import androidx.work.WorkManager
import com.example.data.core.preferences.RevisionPreference
import com.example.data.database.ToDoDao
import com.example.data.database.TodoItemModelDb
import com.example.data.mapper.DtoDbMapper
import com.example.data.mapper.Mapper
import com.example.data.network.RequestResult
import com.example.data.network.RetrofitClient
import com.example.data.network.models.ElementDto
import com.example.data.network.result
import com.example.data.workers.NetworkWorker
import com.example.domain.entity.TodoItemEntity
import com.example.domain.entity.remote.Result
import com.example.domain.repository.TodoItemsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.net.UnknownHostException
import javax.inject.Inject

class TodoItemsRepositoryImpl @Inject constructor(
    private val toDoDao: ToDoDao,
    private val mapper: Mapper,
    private val apiService: RetrofitClient,
    private val dbDtoMapper: DtoDbMapper,
    private val revisionPreference: RevisionPreference,
    private val application: Application
) : TodoItemsRepository {

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

    override fun getItemsList(): Flow<List<TodoItemEntity>> {
        val list = toDoDao.getToDoList().map { listFromDb ->
            listFromDb.map {
                mapper.mapDbModelToEntity(it)
            }
        }
        return list
    }

    override fun completedToDoCount(): Flow<Int> {
        return toDoDao.getToDoList().map { list ->
            list.count {
                it.completed
            }
        }
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

    override suspend fun syncWithCloud(): Result {

        val localList = toDoDao.getToDoListNeFlow()
        Log.d("zyzz", "localList " + localList.toString())
        val serverList = apiService.api.getToDoList().body()?.list
        Log.d("zyzz", "serverList " + serverList.toString())

        val synchronizedList = serverList?.let { mergeServerListWithLocalList(localList, it) }


        if (synchronizedList != null) {
            toDoDao.replaceAll(synchronizedList)
        } else {
            return Result.UNDEFINED_ERROR
        }

        return Result.UNDEFINED_ERROR
    }

    private fun mergeServerListWithLocalList(
        localList: List<TodoItemModelDb>,
        serverList: List<ElementDto>
    ): List<TodoItemModelDb> {
        val idItemMap = serverList.associateBy(ElementDto::id).toMutableMap()
        localList.forEach { oldItem ->
            val newItem = idItemMap[oldItem.id.toString()]
            if (newItem != null && oldItem.dateOfChange > newItem.changed_at)
                idItemMap[newItem.id] = dbDtoMapper.mapDbModelToDto(oldItem)
        }
        val list = idItemMap.values.toList()
        Log.d("zyzz", "${dbDtoMapper.mapListDtoToListModelDb(list)}")
        return dbDtoMapper.mapListDtoToListModelDb(list)
    }

    override suspend fun deleteItem(itemId: Int) {
        toDoDao.deleteToDoItem(itemId)
        updateServer()
    }

    private suspend fun updateServer() {
        val dtoList = dbDtoMapper.mapListModelDbToListDto(toDoDao.getToDoListNeFlow())
        Log.d("zyzz", dtoList.list.toString())
        try {
            val currentRevision = revisionPreference.getRevision()
            val requestUpdate = apiService.api.updateToDoList(currentRevision, dtoList)
            requestUpdate.body()?.let {
                //toDoDao.replaceAll(dbDtoMapper.mapListDtoToListModelDb(it))
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
