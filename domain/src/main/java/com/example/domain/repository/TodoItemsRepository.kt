package com.example.domain.repository

import com.example.domain.entity.TodoItem

interface TodoItemsRepository {
    fun addNewItem(item: TodoItem)
}