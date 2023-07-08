package com.example.domain.usecase

import com.example.domain.repository.TodoItemsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CompletedToDoCountUseCase @Inject constructor(private val repository: TodoItemsRepository) {
    operator fun invoke(): Flow<Int> {
        return repository.completedToDoCount()
    }
}