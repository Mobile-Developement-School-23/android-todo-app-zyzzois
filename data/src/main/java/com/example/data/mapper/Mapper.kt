package com.example.data.mapper

import com.example.data.database.TodoItemModelDb
import com.example.domain.entity.Importance
import com.example.domain.entity.TodoItemEntity
import javax.inject.Inject

class Mapper @Inject constructor() {
    fun mapEntityToDbModel(itemEntity: TodoItemEntity) = TodoItemModelDb(
        id = itemEntity.id,
        text = itemEntity.text,
        importance = itemEntity.importance.toString(),
        deadline = itemEntity.deadline,
        completed = itemEntity.completed,
        dateOfChange = itemEntity.dateOfChange,
        dateOfCreation = itemEntity.dateOfCreation
    )

    fun mapDbModelToEntity(dbModel: TodoItemModelDb) = TodoItemEntity(
        id = dbModel.id,
        text = dbModel.text,
        importance = convertStringToImportance(dbModel.importance),
        deadline = dbModel.deadline,
        completed = dbModel.completed,
        dateOfCreation = dbModel.dateOfCreation,
        dateOfChange = dbModel.dateOfChange
    )

    private fun convertStringToImportance(stringImportance: String): Importance {
        return when (stringImportance) {
            LOW -> Importance.Low
            NORMAL -> Importance.Normal
            URGENT -> Importance.Urgent
            else -> Importance.Normal
        }
    }

    companion object {
        private const val LOW = "Low"
        private const val NORMAL = "Normal"
        private const val URGENT = "Urgent"
    }
}
