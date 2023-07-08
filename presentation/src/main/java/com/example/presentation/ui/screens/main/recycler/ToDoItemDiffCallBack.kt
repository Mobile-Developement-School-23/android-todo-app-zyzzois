package com.example.presentation.ui.screens.main.recycler

import androidx.recyclerview.widget.DiffUtil
import com.example.domain.entity.TodoItemEntity

object ToDoItemDiffCallBack : DiffUtil.ItemCallback<TodoItemEntity>() {
    override fun areItemsTheSame(oldItem: TodoItemEntity, newItem: TodoItemEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: TodoItemEntity, newItem: TodoItemEntity): Boolean {
        return oldItem == newItem
    }
}
