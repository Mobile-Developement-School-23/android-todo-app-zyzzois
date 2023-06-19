package com.example.domain.entity

data class TodoItemEntity (
    val id: Int = UNDEFINED_ID,
    val text: String,
    val importance: Importance,
    val deadline: Long = UNDEFINED_DATE,
    val completed: Boolean,
    val dateOfCreation: Long,
    val dateOfChange: Long = UNDEFINED_DATE
) {
    companion object{
        const val UNDEFINED_ID = 0
        const val UNDEFINED_DATE = -1L
    }
}