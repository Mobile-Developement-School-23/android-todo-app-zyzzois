package com.example.domain.usecase

import com.example.domain.entity.TodoItemEntity
import com.example.domain.repository.TodoItemsRepository
import javax.inject.Inject

class GetItemsListUseCase @Inject constructor(private val repository: TodoItemsRepository) {
    suspend operator fun invoke(): List<TodoItemEntity> {
        return repository.getItemsList()
    }
}