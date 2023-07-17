package com.example.presentation.ui.screens.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.Importance
import com.example.domain.entity.TodoItemEntity
import com.example.domain.entity.TodoItemEntity.Companion.UNDEFINED_DATE
import com.example.domain.entity.TodoItemEntity.Companion.UNDEFINED_ID
import com.example.domain.usecase.AddNewItemUseCase
import com.example.domain.usecase.DeleteItemUseCase
import com.example.domain.usecase.EditItemUseCase
import com.example.domain.usecase.GetItemByIdUseCase
import com.example.presentation.ui.util.Constants.MODE_ADD
import com.example.presentation.ui.util.Converter.convertStringToImportance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailViewModel @Inject constructor(
    private val getItemByIdUseCase: GetItemByIdUseCase,
    private val addNewItemUseCase: AddNewItemUseCase,
    private val deleteItemUseCase: DeleteItemUseCase,
    private val editItemUseCase: EditItemUseCase
) : ViewModel() {
    private var isEditing = false


    var toDoItemEntityId = UNDEFINED_ID
    var tempValueForDeadline = UNDEFINED_DATE

    private val _errorInputText = MutableLiveData<Boolean>()
    val errorInputText: LiveData<Boolean>
        get() = _errorInputText

    private val _itemEntity = MutableLiveData<TodoItemEntity>()
    val itemEntity: LiveData<TodoItemEntity>
        get() = _itemEntity

    fun getToDoItem(itemId: Int) {
        viewModelScope.launch {
            val item = getItemByIdUseCase(itemId)
            //_state.value = State(itemEntity = item)
            _itemEntity.value = item
        }
    }

    fun addToDoItem(text: String?, importance: String, deadline: Long) {
        val content = parseName(text)
        val fieldsValid = validateInput(content)
        if (fieldsValid) {
            viewModelScope.launch {
                val toDoItemEntity = TodoItemEntity(
                    text = content,
                    deadline = deadline,
                    importance = convertStringToImportance(importance),
                    dateOfCreation = System.currentTimeMillis(),
                    completed = false
                )
                addNewItemUseCase(toDoItemEntity)
            }
        }
    }

    fun editToDoItem(text: String?, importance: Importance, deadline: Long) {
        val content = parseName(text)
        val fieldsValid = validateInput(content)
        if (fieldsValid) {
            _itemEntity.value?.let {
                viewModelScope.launch(Dispatchers.IO) {
                    val toDoItemEntity = it.copy(
                        text = content,
                        importance = importance,
                        deadline = deadline,
                        dateOfChange = System.currentTimeMillis()
                    )
                    editItemUseCase(toDoItemEntity)
                }
            }
        }
    }

    fun deleteToDoItem(itemId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteItemUseCase(itemId)
        }
    }

    private fun parseName(inputName: String?): String {
        return inputName?.trim() ?: ""
    }

    private fun validateInput(name: String): Boolean {
        var result = true
        if (name.isBlank()) {
            //_state.value = State(isError = true)
            _errorInputText.value = true
            result = false
        }
        return result
    }


    fun resetErrorInputText() {
        //_state.value = State(isError = false)
        _errorInputText.value = false
    }


}
