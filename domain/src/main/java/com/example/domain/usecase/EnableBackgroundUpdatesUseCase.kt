package com.example.domain.usecase

import com.example.domain.repository.TodoItemsRepository
import javax.inject.Inject

class EnableBackgroundUpdatesUseCase @Inject constructor(
    private val repository: TodoItemsRepository
) {
    suspend operator fun invoke() {
        repository.enableBackGroundUpdates()
    }
}