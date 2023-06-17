package com.example.todo.ui.recycler

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.entity.Importance
import com.example.domain.entity.TodoItemEntity
import com.example.todo.R
import com.example.todo.databinding.ListItemBinding

class ToDoListAdapter: ListAdapter<TodoItemEntity, ToDoListAdapter.ToDoItemViewHolder>(ToDoItemDiffCallBack) {

    var onItemLongClickListener: ((TodoItemEntity) -> Unit)? = null
    var onItemClickListener: ((TodoItemEntity) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoItemViewHolder {
        val binding = ListItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ToDoItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ToDoItemViewHolder, position: Int) {
        val context = holder.itemView.context
        val toDoItem = getItem(position)
        val binding = holder.binding

        binding.root.setOnLongClickListener {
            onItemLongClickListener?.invoke(toDoItem)
            true
        }
        binding.root.setOnClickListener {
            onItemClickListener?.invoke(toDoItem)
        }

        with(binding) {
            icon.setImageResource(toDoItem.importance.toResource())
            if (toDoItem.completed) {
                checkbox.isChecked = true
                tvContent.setTextColor(ContextCompat.getColor(context, R.color.light_gray))
                tvContent.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            }
            when (toDoItem.importance) {
                Importance.Normal -> tvContent.text = toDoItem.text
                Importance.Low -> tvContent.text = toDoItem.text
                Importance.Urgent -> tvContent.text = toDoItem.text
                else -> {}
            }
        }
    }

    inner class ToDoItemViewHolder(val binding: ListItemBinding):
        RecyclerView.ViewHolder(binding.root)

    private fun Importance.toResource() = when(this) {
        Importance.Low -> R.drawable.ic_priority_low_24dp
        Importance.Urgent -> R.drawable.ic_priority_high_24dp
        else -> 0
    }
}