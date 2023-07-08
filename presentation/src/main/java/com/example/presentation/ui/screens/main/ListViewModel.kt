package com.example.presentation.ui.screens.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.TodoItemEntity
import com.example.domain.entity.remote.Result
import com.example.domain.usecase.CompletedToDoCountUseCase
import com.example.domain.usecase.DeleteItemUseCase
import com.example.domain.usecase.EditItemUseCase
import com.example.domain.usecase.GetItemsListUseCase
import com.example.domain.usecase.LoadDataUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class ListViewModel @Inject constructor(
    private val getToDoListUseCase: GetItemsListUseCase,
    private val deleteItemUseCase: DeleteItemUseCase,
    private val editItemUseCase: EditItemUseCase,
    private val loadDataUseCase: LoadDataUseCase,
    private val completedCount: CompletedToDoCountUseCase
) : ViewModel() {

    private val _requestResult = MutableLiveData<Result?>()
    val requestResult: LiveData<Result?>
        get() = _requestResult

    fun toDoList() = getToDoListUseCase()
    fun completedToDoNumber() = completedCount()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _requestResult.value = loadDataUseCase()
        }
    }

    fun editToDoItem(item: TodoItemEntity, completed: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            val toDoItemEntity = item.copy(completed = completed)
            editItemUseCase(toDoItemEntity)
        }
    }

    fun deleteToDoItem(todoItemId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteItemUseCase(todoItemId)
        }
    }
}
