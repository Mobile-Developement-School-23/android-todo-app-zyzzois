package com.example.domain.usecase

import com.example.domain.repository.TodoItemsRepository
import javax.inject.Inject

class UploadToDoToServerUseCase @Inject constructor(private val repository: TodoItemsRepository) {
    suspend operator fun invoke() {
        repository.uploadToDoToServer()
    }
}