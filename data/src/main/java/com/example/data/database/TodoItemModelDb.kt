package com.example.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todos")
data class TodoItemModelDb(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    val text: String,
    val importance: String,
    val deadline: Long,
    val completed: Boolean,
    val dateOfCreation: Long,
    val dateOfChange: Long
)