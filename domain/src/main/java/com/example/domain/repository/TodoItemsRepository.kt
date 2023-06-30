package com.example.domain.repository

import com.example.domain.entity.TodoItemEntity
import com.example.domain.entity.remote.Result

interface TodoItemsRepository {
    suspend fun addNewItem(item: TodoItemEntity)
    suspend fun getItemById(itemId: Int): TodoItemEntity
    suspend fun editItem(item: TodoItemEntity)
    suspend fun deleteItem(itemId: Int)
    suspend fun getItemsList(): List<TodoItemEntity>
    suspend fun loadData(): Result
}