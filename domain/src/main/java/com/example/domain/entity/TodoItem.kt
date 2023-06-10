package com.example.domain.entity

sealed class Importance {
    object Low: Importance()
    object Normal: Importance()
    object Urgent: Importance()
}

data class TodoItem(
    val id: String,
    val text: String,
    val importance: Importance,
    val deadline: Long,
    val completed: Boolean,
    val dateOfCreation: Long,
    val dateOfChange: Long
)
