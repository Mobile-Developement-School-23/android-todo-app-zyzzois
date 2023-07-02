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
            BASIC -> Importance.Basic
            IMPORTANT -> Importance.Important
            else -> Importance.Basic
        }
    }

    companion object {
        const val LOW = "low"
        const val BASIC = "basic"
        const val IMPORTANT = "important"
    }
}
