package com.example.domain.usecase

import com.example.domain.entity.TodoItemEntity
import com.example.domain.repository.TodoItemsRepository
import javax.inject.Inject

class EditItemUseCase @Inject constructor(private val repository: TodoItemsRepository) {
    suspend operator fun invoke(item: TodoItemEntity) {
        repository.editItem(item)
    }
}