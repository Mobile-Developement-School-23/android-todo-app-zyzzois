package com.example.domain.usecase

import com.example.domain.repository.TodoItemsRepository
import javax.inject.Inject

class DeleteItemUseCase @Inject constructor(private val repository: TodoItemsRepository) {
    suspend operator fun invoke(itemId: Int) {
        repository.deleteItem(itemId)
    }
}