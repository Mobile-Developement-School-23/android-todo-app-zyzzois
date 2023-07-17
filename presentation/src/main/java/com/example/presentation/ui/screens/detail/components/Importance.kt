package com.example.presentation.ui.screens.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.example.domain.entity.Importance
import com.example.presentation.R
import com.example.presentation.ui.theme.Red
import com.example.presentation.ui.theme.TodoAppCustomTheme

@Composable
fun Importance(importance: MutableState<Importance>) {

    val menuExpanded = remember {
        mutableStateOf(false)
    }
    val isImportant = remember(importance) {
        importance.value == Importance.Basic
    }

    Column(
        modifier = Modifier
            .padding(top = 24.dp, bottom = 12.dp)
            .padding(horizontal = 12.dp)
            .clip(RoundedCornerShape(6.dp))
            .fillMaxWidth()
            .clickable {
                menuExpanded.value = true
            }
            .padding(4.dp)
    ) {
        val text = when (importance.value) {
            Importance.Basic -> stringResource(id = R.string.no_importance)
            Importance.Important -> stringResource(id = R.string.high_importance)
            Importance.Low -> stringResource(id = R.string.low_importance)
        }
        Text(
            text = stringResource(id = R.string.importance_text),
            color = TodoAppCustomTheme.colors.labelPrimary,
            style = MaterialTheme.typography.body1.copy(
                fontFamily = FontFamily(Font(R.font.vk_sans_display_medium))
            )
        )
        Text(
            text = text,
            modifier = Modifier.padding(top = 4.dp),
            color = if(isImportant) Red else TodoAppCustomTheme.colors.labelTertiary,
            style = MaterialTheme.typography.body1.copy(
                fontFamily = FontFamily(Font(R.font.vk_sans_display_medium))
            )
        )

        TodoImportanceDropdownMenu(
            menuExpanded.value,
            hideMenu = { menuExpanded.value = false },
            importance)
    }
}


@Composable
fun TodoImportanceDropdownMenu(
    menuExpanded: Boolean,
    hideMenu: () -> Unit,
    importance: MutableState<Importance>){
    DropdownMenu(
        expanded = menuExpanded,
        onDismissRequest = { hideMenu() },
        modifier = Modifier.background(TodoAppCustomTheme.colors.backElevated),
        offset = DpOffset(x = 52.dp, y = (-18).dp)
    ) {

        DropdownMenuItem(
            text = { Text(stringResource(id = R.string.no_importance)) },
            onClick = {
                importance.value = Importance.Basic
                hideMenu()
            }
        )
        DropdownMenuItem(
            text = { Text(stringResource(id = R.string.low_importance)) },
            onClick = {
                importance.value = Importance.Low
                hideMenu()
            }
        )
        DropdownMenuItem(
            text = { Text(stringResource(id = R.string.high_importance)) },
            onClick = {
                importance.value = Importance.Important
                hideMenu()
            }
        )
    }

}

@Preview
@Composable
private fun ImportancePreview(){
    val importance = remember {
        mutableStateOf(Importance.Important)
    }
    Importance(importance)
}