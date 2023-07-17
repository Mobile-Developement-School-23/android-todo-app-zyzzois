package com.example.presentation.ui.screens.detail.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.example.presentation.R
import com.example.presentation.ui.theme.Red
import com.example.presentation.ui.theme.TodoAppCustomTheme

@Composable
fun ButtonDelete(
    enabled: Boolean,
    onClick: () -> Unit,
) {
    val deleteButtonColor by animateColorAsState(
        targetValue = if (enabled) Red else TodoAppCustomTheme.colors.labelDisable,
    )

    TextButton(
        onClick = { onClick() },
        modifier = Modifier.padding(horizontal = 4.dp),
        enabled = enabled,
        colors = ButtonDefaults.textButtonColors(
            contentColor = if (enabled) Red else TodoAppCustomTheme.colors.labelDisable,
            disabledContentColor = if (enabled) Red else TodoAppCustomTheme.colors.labelDisable
        )
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "удалить",
            modifier = Modifier.size(26.dp)
        )
        Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
        Text(
            text = "удалить",
            color = deleteButtonColor,
            style = MaterialTheme.typography.body1.copy(
                fontFamily = FontFamily(Font(R.font.vk_sans_display_medium))
            )
        )
    }
}
