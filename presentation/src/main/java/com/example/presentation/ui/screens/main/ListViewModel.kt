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
import com.example.domain.usecase.SyncWithCloudUseCase
import com.example.presentation.ui.core.network.ConnectionListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class ListViewModel @Inject constructor(
    private val getToDoListUseCase: GetItemsListUseCase,
    private val deleteItemUseCase: DeleteItemUseCase,
    private val editItemUseCase: EditItemUseCase,
    private val loadDataUseCase: LoadDataUseCase,
    private val completedCount: CompletedToDoCountUseCase,
    private val connectionListener: ConnectionListener,
    private val syncWithCloudUseCase: SyncWithCloudUseCase
) : ViewModel() {

    private val _shouldShowNetworkError = MutableLiveData<Boolean>()
    val shouldShowNetworkError: LiveData<Boolean>
        get() = _shouldShowNetworkError

    init {
        startListeningToConnection()
    }

    private fun startListeningToConnection() {
        connectionListener.observeForever { connected ->
            if (connected) {
                _shouldShowNetworkError.value = false
                syncWithCloud()
                //loadData()
            } else {
                if (_shouldShowNetworkError.value != false)
                    _shouldShowNetworkError.value = true
            }
        }
    }

    private val _requestResult = MutableLiveData<Result?>()
    val requestResult: LiveData<Result?>
        get() = _requestResult


    fun toDoList() = getToDoListUseCase()
    fun completedToDoNumber() = completedCount()

    fun loadData() {
        viewModelScope.launch {
            _requestResult.value = loadDataUseCase()
        }
    }

    private fun syncWithCloud() {
        viewModelScope.launch {
            syncWithCloudUseCase()
        }
    }

    fun editToDoItem(item: TodoItemEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            val isDone= !item.completed
            val toDoItemEntity = item.copy(completed = isDone)
            editItemUseCase(toDoItemEntity)
        }
    }

    fun deleteToDoItem(todoItemId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteItemUseCase(todoItemId)
        }
    }
}
