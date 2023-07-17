package com.example.presentation.ui.screens.detail.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Dp
import com.example.presentation.R
import com.example.presentation.ui.theme.Blue
import com.example.presentation.ui.theme.LightBlue
import com.example.presentation.ui.theme.TodoAppCustomTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    enabled: Boolean,
    elevation: Dp,
    onClick:   () -> Unit,
    onClickClose:   () -> Unit
) {
    TopAppBar(
        modifier = Modifier.shadow(elevation),
        navigationIcon = {
            IconButton(
                onClick = { onClickClose() }
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                )
            }
        },
        title = {},
        actions = {
            val saveColor by animateColorAsState(
                targetValue = if (enabled) Blue else TodoAppCustomTheme.colors.labelDisable)

            TextButton(
                onClick = { onClick() },
                enabled = enabled,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = if (enabled) Blue else TodoAppCustomTheme.colors.labelDisable,
                    disabledContentColor = if (enabled) Blue else TodoAppCustomTheme.colors.labelDisable
                )
            ) {
                Text(
                    text = stringResource(R.string.save_button_text),
                    color = LightBlue,
                    style = androidx.compose.material.MaterialTheme.typography.body1.copy(
                        fontFamily = FontFamily(Font(R.font.vk_sans_display_medium))
                    )
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = TodoAppCustomTheme.colors.backPrimary,
            navigationIconContentColor = TodoAppCustomTheme.colors.labelPrimary
        )
    )
}