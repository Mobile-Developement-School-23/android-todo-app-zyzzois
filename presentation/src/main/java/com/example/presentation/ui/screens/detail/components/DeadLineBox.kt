package com.example.presentation.ui.screens.detail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.R

@Composable
fun DeadlineBox(
    checkedState: MutableState<Boolean>,
    showDialog: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            Text(
                text = stringResource(R.string.do_before),
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier
                    .padding(start = 18.dp, top = 16.dp),
                color = MaterialTheme.colors.primary
            )
            Text(
                text = stringResource(R.string.hint_no),
                modifier = Modifier
                    .padding(top = 4.dp, start = 18.dp),
                color = MaterialTheme.colors.primary
            )
        }

        Switch(
            modifier = Modifier.padding(top = 26.dp, end = 18.dp),
            checked = checkedState.value,
            onCheckedChange = { newCheckedState ->
               if (newCheckedState)
                   showDialog()
               checkedState.value = newCheckedState
            }
        )

    }
}