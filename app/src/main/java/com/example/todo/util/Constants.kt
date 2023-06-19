package com.example.todo.util

import com.example.domain.entity.Importance

object Constants {
    const val BINDING_NULL_EXCEPTION_MESSAGE = "Binding = null"
    const val PICKER_TAG = "picker_tag"
    const val MODE_EDIT = "mode_edit"
    const val MODE_ADD = "mode_add"
    const val MODE_UNKNOWN = "mode_unknown"
    const val DEFAULT_ID = -9999
    const val PARAM_TODO_ITEM_ID_IS_ABSENT_EXCEPTION_MESSAGE = "Param todo item id is absent"
    const val UNKNOWN_SCREEN_MODE = "Unknown screen mode"
    const val TODO_DELETED = "Запись удалена"
    const val COMPLETED = "Выполнено - %d"
}

object Converter {
    fun convertStringToImportance(stringImportance: String): Importance {
        return when (stringImportance) {
            "Низкий" -> Importance.Low
            "Нет" -> Importance.Normal
            "!! Высокий" -> Importance.Urgent
            else -> Importance.Normal
        }
    }
}