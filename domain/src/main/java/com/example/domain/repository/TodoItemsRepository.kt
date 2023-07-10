package com.example.domain.repository

import com.example.domain.entity.TodoItemEntity
import com.example.domain.entity.remote.Result
import kotlinx.coroutines.flow.Flow

interface TodoItemsRepository {
    suspend fun addNewItem(item: TodoItemEntity)
    suspend fun getItemById(itemId: Int): TodoItemEntity
    suspend fun editItem(item: TodoItemEntity)
    suspend fun deleteItem(itemId: Int)
    suspend fun enableBackGroundUpdates()
    suspend fun loadData(): Result
    suspend fun syncWithCloud(): Result
    fun getItemsList(): Flow<List<TodoItemEntity>>
    fun completedToDoCount(): Flow<Int>
}
