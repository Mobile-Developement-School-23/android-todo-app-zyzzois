package com.example.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = White,
    primaryVariant = White,
    background = DarkBackground,
    onSecondary = White,
    onBackground = White,
    onSurface = White,
    surface = Color.Black
)

private val LightColorPalette = lightColors(
    background = LightBackground,
    primary = Black,
    primaryVariant = White,
    onSecondary = Black,
    onSurface = Black,
    onBackground = Black
)

@Composable
fun TodoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorPalette else LightColorPalette

    MaterialTheme(
        colors = colors,
        content = content
    )
}