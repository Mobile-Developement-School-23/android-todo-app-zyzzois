package com.example.presentation.ui.screens.detail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.presentation.R
import com.example.presentation.ui.screens.detail.DetailViewModel
import com.example.presentation.ui.theme.LightBlue

@Composable
fun Header(
    viewModel: DetailViewModel
) {
    Row (
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(
            onClick = {  },
            modifier = Modifier
                .padding(top = 48.dp, start = 16.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_cancel),
                contentDescription = "Иконка"
            )
        }
        TextButton(
            onClick = {

            },
            modifier = Modifier
                .height(IntrinsicSize.Min)
                .padding(end = 18.dp, top = 48.dp),
            colors = ButtonDefaults.textButtonColors(contentColor = LightBlue)
        ) {
            Text(
                text = stringResource(R.string.save_button_text),
                fontWeight = FontWeight.Bold
            )
        }
    }
}