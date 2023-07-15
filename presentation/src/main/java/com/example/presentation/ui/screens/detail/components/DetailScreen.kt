package com.example.presentation.ui.screens.detail.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.domain.entity.TodoItemEntity.Companion.UNDEFINED_DATE
import com.example.presentation.ui.screens.detail.DetailViewModel

@Composable
fun DetailScreen(
    viewModel: DetailViewModel
) {

    val todoText = remember { mutableStateOf("") }
    val checkedState = remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    val selectedDate = remember { mutableLongStateOf(UNDEFINED_DATE) }

    if (checkedState.value)
        DatePickerComponent(
            show = showDialog,
            viewModel = viewModel,
            closeScreen = { showDialog = false }
        )

    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            Header(viewModel)
            TextInputEditText(viewModel, todoText)
            PriorityBox()
            FirstDivider()
            DeadlineBox(checkedState) { showDialog = true }
            SecondDivider()
            DeleteButton()
        }
    }
}



