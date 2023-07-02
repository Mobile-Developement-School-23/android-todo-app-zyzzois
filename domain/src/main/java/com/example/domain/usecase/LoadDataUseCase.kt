package com.example.domain.usecase

import com.example.domain.entity.remote.Result
import com.example.domain.repository.TodoItemsRepository
import javax.inject.Inject

class LoadDataUseCase @Inject constructor(private val repository: TodoItemsRepository) {
    suspend operator fun invoke(): Result {
        return repository.loadData()
    }
}