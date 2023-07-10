package com.example.presentation.ui.screens.main.recycler

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.entity.Importance
import com.example.domain.entity.TodoItemEntity
import com.example.presentation.R
import com.example.presentation.databinding.ListItemBinding

class ToDoListAdapter : ListAdapter<TodoItemEntity, ToDoListAdapter.ToDoItemViewHolder>(
    ToDoItemDiffCallBack
) {

    var onItemLongClickListener: ((TodoItemEntity) -> Unit)? = null
    var onItemClickListener: ((TodoItemEntity) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoItemViewHolder {
        val binding = ListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
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
                Importance.Important -> {
                    tvContent.text = toDoItem.text
                    checkbox.buttonDrawable =
                        AppCompatResources.getDrawable(context, R.drawable.checkbox_high_importance)
                    checkbox.buttonTintList =
                        ContextCompat.getColorStateList(context, R.color.checkbox_important_tint)
                }
                else -> {
                    tvContent.text = toDoItem.text
                    checkbox.buttonTintList =
                        ContextCompat.getColorStateList(context, R.color.checkbox_basic_tint)
                }
            }
        }
    }

    private fun Importance.toResource() = when (this) {
        Importance.Low -> R.drawable.ic_priority_low_24dp
        Importance.Important -> R.drawable.ic_priority_high_24dp
        else -> 0
    }

    public override fun getItem(position: Int): TodoItemEntity {
        return super.getItem(position)
    }

    inner class ToDoItemViewHolder(val binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}
