package com.example.domain.usecase

import com.example.domain.entity.TodoItemEntity
import com.example.domain.repository.TodoItemsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetItemsListUseCase @Inject constructor(private val repository: TodoItemsRepository) {
    operator fun invoke(): Flow<List<TodoItemEntity>> {
        return repository.getItemsList()
    }
}