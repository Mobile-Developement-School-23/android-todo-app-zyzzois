package com.example.presentation.ui.screens.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.R

@Composable
fun DeleteButton() {
    Button(
        onClick = { /* Handle delete button click */ },
        modifier = Modifier
            .padding(top = 12.dp, start = 18.dp)
            .background(MaterialTheme.colors.background)
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_delete),
            contentDescription = stringResource(R.string.delete),
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = stringResource(R.string.delete),
            fontSize = 16.sp,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}