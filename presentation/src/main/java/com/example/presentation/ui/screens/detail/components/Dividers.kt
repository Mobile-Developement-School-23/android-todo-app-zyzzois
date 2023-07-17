package com.example.presentation.ui.screens.detail.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.presentation.ui.theme.TodoAppCustomTheme

@Composable
fun MyDivider(padding: PaddingValues) {
    Divider(
        modifier = Modifier
            .fillMaxWidth()
            .padding(padding),
        color = TodoAppCustomTheme.colors.supportSeparator
    )
}