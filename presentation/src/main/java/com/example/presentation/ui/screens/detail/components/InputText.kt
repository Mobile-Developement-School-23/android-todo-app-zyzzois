package com.example.presentation.ui.screens.detail.components

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.presentation.R
import com.example.presentation.ui.theme.TodoAppCustomTheme

@Composable
fun InputText(valueState: MutableState<String>){

    OutlinedTextField(
        value = valueState.value,
        onValueChange = { newValue ->
            valueState.value = newValue
        },
        label = {
            Text(
                text = "Что надо сделать",
                color = TodoAppCustomTheme.colors.labelPrimary,
                style = MaterialTheme.typography.body1.copy(
                    fontFamily = FontFamily(Font(R.font.vk_sans_display_medium))
                )
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 42.dp)
            .defaultMinSize(minHeight = 112.dp)
    )
}

@Preview
@Composable
fun TextFieldPreview() {
    val rawValue = remember { mutableStateOf("") }
    InputText(rawValue)
}