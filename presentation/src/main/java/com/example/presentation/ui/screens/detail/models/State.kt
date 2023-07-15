package com.example.presentation.ui.screens.detail.models

import com.example.domain.entity.TodoItemEntity

data class State(
    val itemEntity: TodoItemEntity? = null,
    val isEditing: Boolean = false,
    val isError: Boolean = false
)