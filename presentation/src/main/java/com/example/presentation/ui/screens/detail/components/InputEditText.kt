package com.example.presentation.ui.screens.detail.components

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.presentation.R
import com.example.presentation.ui.screens.detail.DetailViewModel

@Composable
fun TextInputEditText(
    viewModel: DetailViewModel,
    textFieldValue: MutableState<String>
) {
    OutlinedTextField(
        value = "",
        onValueChange = { newValue ->

            textFieldValue.value = newValue
        },
        label = { Text(text = stringResource(R.string.hint_text)) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 42.dp)
            .defaultMinSize(minHeight = 112.dp)
    )
}