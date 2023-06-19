package com.example.data.repository

import com.example.data.database.ToDoDao
import com.example.data.mapper.Mapper
import com.example.domain.entity.TodoItemEntity
import com.example.domain.repository.TodoItemsRepository
import javax.inject.Inject

class TodoItemsRepositoryImpl @Inject constructor(
    private val toDoDao: ToDoDao,
    private val mapper: Mapper
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

    override suspend fun deleteItem(itemId: Int) {
        toDoDao.deleteToDoItem(itemId)
    }
}