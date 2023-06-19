package com.example.data.database

import androidx.room.*

@Dao
interface ToDoDao {

    @Query("SELECT * FROM todos")
    suspend fun getToDoList(): List<TodoItemModelDb>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTodo(toDoItem: TodoItemModelDb)

    @Query("SELECT * FROM todos WHERE id=:itemId LIMIT 1")
    suspend fun getItemById(itemId: Int): TodoItemModelDb

    @Query("DELETE FROM todos WHERE id=:itemId")
    suspend fun deleteToDoItem(itemId: Int)

}