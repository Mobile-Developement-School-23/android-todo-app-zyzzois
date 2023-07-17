package com.example.presentation.ui.screens.detail.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.example.presentation.R
import com.example.presentation.ui.theme.TodoAppCustomTheme
import com.example.presentation.ui.util.convertLongDeathlineToString
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState

@Composable
fun TodoDataPicker(
    date: MutableState<Long>,
    dateString: MutableState<String>,
    isDateVisible: MutableState<Boolean>,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 16.dp)
            .height(100.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        var openData by remember { mutableStateOf(false) }
        val clockState = rememberUseCaseState()

        DatePicker(
            date = date,
            enable = openData,
            closeFun = { openData = false },
        )

        Column {
            Text(
                text = stringResource(id = R.string.do_before),
                modifier = Modifier.padding(start = 4.dp),
                color = TodoAppCustomTheme.colors.labelPrimary,
                style = androidx.compose.material.MaterialTheme.typography.body1.copy(
                    fontFamily = FontFamily(Font(R.font.vk_sans_display_medium))
                )
            )

            AnimatedVisibility(visible = isDateVisible.value) {
                Column() {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable {
                                openData = true
                            }
                            .padding(6.dp)
                    ) {
                        Text(text = convertLongDeathlineToString(date.value), color = Blue)
                    }
                }
            }
        }

        Switch(
            checked = isDateVisible.value,
            onCheckedChange = { isDateVisible.value = !isDateVisible.value },
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatePicker(
    date: MutableState<Long>,
    enable: Boolean,
    closeFun: () -> Unit,
) {
    if (enable) {
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = date.value)
        val confirmEnable = remember(datePickerState.selectedDateMillis) {
            datePickerState.selectedDateMillis != null
        }

        DatePickerDialog(
            onDismissRequest = closeFun,
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let {
                            date.value = it
                        }
                        closeFun()
                    },
                    enabled = confirmEnable
                ) {
                    Text("ok")
                }
            },
            dismissButton = {
                TextButton( onClick = closeFun ) {
                    Text("отмена")
                }
            }
        ) {
            androidx.compose.material3.DatePicker(state = datePickerState)
        }
    }
}

