package com.example.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * Интерфейс для взаимодействия с БД, тут описаны все основые запросы к БД и возвращаемый тип,
 * соответствующий каждому запросу
 */

@Dao
interface ToDoDao {

    @Query("SELECT * FROM todos")
    fun getToDoList(): Flow<List<TodoItemModelDb>>

    @Query("SELECT * FROM todos")
    suspend fun getToDoListNeFlow(): List<TodoItemModelDb>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTodo(toDoItem: TodoItemModelDb)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateList(toDoList: List<TodoItemModelDb>)

    @Query("SELECT * FROM todos WHERE id=:itemId LIMIT 1")
    suspend fun getItemById(itemId: Int): TodoItemModelDb

    @Query("DELETE FROM todos WHERE id=:itemId")
    suspend fun deleteToDoItem(itemId: Int)

    @Query("DELETE FROM todos")
    suspend fun clearTable()
}
