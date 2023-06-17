package com.example.todo.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.TodoItemEntity
import com.example.domain.usecase.DeleteItemUseCase
import com.example.domain.usecase.EditItemUseCase
import com.example.domain.usecase.GetItemsListUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class ListViewModel @Inject constructor(
    private val getToDoListUseCase: GetItemsListUseCase,
    private val deleteItemUseCase: DeleteItemUseCase,
    private val editItemUseCase: EditItemUseCase
): ViewModel() {

    private val _toDoList = MutableLiveData<List<TodoItemEntity>?>()
    val toDoList: LiveData<List<TodoItemEntity>?>
        get() = _toDoList

    private val _completedNumber = MutableLiveData<Int>()
    val completedNumber: LiveData<Int>
        get() = _completedNumber

    fun updateList() {
        viewModelScope.launch {
            _toDoList.value = getToDoListUseCase()
        }
    }

    fun editToDoItem(item: TodoItemEntity, completed: Boolean) {
        viewModelScope.launch {
            val toDoItemEntity = item.copy(completed = completed)
            editItemUseCase(toDoItemEntity)
            updateList()
        }
    }


    fun deleteToDoItem(todoItemId: Int) {
        viewModelScope.launch {
            deleteItemUseCase(todoItemId)
        }
        updateList()
    }

    fun updateCompletedNumber() {
        var counter = 0
        for (todo in _toDoList.value!!) {
            if (todo.completed)
                counter += 1
        }
        _completedNumber.value = counter
    }

}