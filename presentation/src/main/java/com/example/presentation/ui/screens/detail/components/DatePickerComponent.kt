package com.example.presentation.ui.screens.detail.components

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import com.example.presentation.ui.screens.detail.DetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerComponent(
    show: Boolean,
    closeScreen: () -> Unit,
    viewModel: DetailViewModel
) {


    if (show) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = closeScreen,
            confirmButton = {
                TextButton(
                    onClick = {
                        closeScreen()
                    }
                ) { Text(text = "Сохранить") }
            },
            dismissButton = {
                TextButton(onClick = closeScreen) { Text(text = "Отменить") }
            }
        ) { DatePicker(state = datePickerState) }

    }

}

