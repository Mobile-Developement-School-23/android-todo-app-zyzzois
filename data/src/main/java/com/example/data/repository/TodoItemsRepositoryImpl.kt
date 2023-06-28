package com.example.data.repository

import android.util.Log
import com.example.data.database.ToDoDao
import com.example.data.mapper.Mapper
import com.example.data.network.ApiService
import com.example.data.network.models.ElementDto
import com.example.data.network.models.ToDoItemDto
import com.example.data.network.models.ToDoListDto
import com.example.domain.entity.TodoItemEntity
import com.example.domain.repository.TodoItemsRepository
import java.util.Calendar
import java.util.UUID
import javax.inject.Inject

class TodoItemsRepositoryImpl @Inject constructor(
    private val toDoDao: ToDoDao,
    private val mapper: Mapper,
    private val apiService: ApiService
): TodoItemsRepository {

    override suspend fun addNewItem(item: TodoItemEntity) {
        toDoDao.addTodo(mapper.mapEntityToDbModel(item))
    }

    override suspend fun getItemById(itemId: Int): TodoItemEntity {
        val dbItem = toDoDao.getItemById(itemId)
        return mapper.mapDbModelToEntity(dbItem)
    }

    override suspend fun editItem(item: TodoItemEntity) {
        toDoDao.addTodo(mapper.mapEntityToDbModel(item))
    }

    override suspend fun getItemsList() = toDoDao.getToDoList().map {
        mapper.mapDbModelToEntity(it)
    }

    override suspend fun loadData() {
        Log.d("zyzz", "override suspend fun loadData() entry")
        try {
            val requestResult = apiService.getToDoList()
            Log.d("zyzz", requestResult.toString())
        } catch (e: Exception) {
            Log.d("zyzz", "override suspend fun loadData() error")
            e.stackTraceToString()
        }
    }

    override suspend fun uploadToDoToServer() {

        Log.d(TAG, "override suspend fun uploadToDoToServer() entry")
        val item = ToDoItemDto(
            ElementDto(
                UUID.randomUUID(),
                "text",
                "low",
                0L,
                true,
                "#FFFFFF",
                0L,
                0L,
                "123"
            ),
            0,
            "ok"
        )
        val tmpList = listOf(item)

        val toDoListDto = ToDoListDto(tmpList, 0, "0")

        try {
            Log.d(TAG, "current list = ${apiService.getToDoList()}")
            apiService.uploadToDoElement(item)


        } catch (e: Exception) {
            Log.d(TAG, "override suspend fun uploadToDoToServer() error")
            e.stackTraceToString()
        }
    }

    override suspend fun deleteItem(itemId: Int) {
        toDoDao.deleteToDoItem(itemId)
    }

    companion object {
        private const val TAG = "zyzz"
    }
}