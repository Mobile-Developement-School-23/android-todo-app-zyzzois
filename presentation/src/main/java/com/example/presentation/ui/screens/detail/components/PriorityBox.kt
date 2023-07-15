package com.example.presentation.ui.screens.detail.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.R

@Composable
fun PriorityBox() {
    Box (
        modifier = Modifier
            .padding(top = 42.dp, start = 18.dp, end = 18.dp)
    ) {
        Column {
            Text(
                text = stringResource(R.string.importance_text),
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colors.primary
            )
            Text(
                text = stringResource(R.string.hint_no),
                fontSize = 16.sp,
                modifier = Modifier.padding(top = 4.dp),
                color = MaterialTheme.colors.primary
            )

        }
    }
}