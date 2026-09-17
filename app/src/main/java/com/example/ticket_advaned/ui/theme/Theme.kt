package com.example.ticket_advaned.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView

private val LightColorScheme = lightColorScheme(
    primary = GojekGreen,
    onPrimary = Color.White,
    primaryContainer = GojekGreenContainer,
    onPrimaryContainer = GojekGreenDark,
    secondary = GojekGreenDark,
    onSecondary = Color.White,
    background = Color(0xFFF8F9FA),
    surface = Color.White,
    onBackground = Color(0xFF1F1F1F),
    onSurface = Color(0xFF1F1F1F)
)

private val DarkColorScheme = lightColorScheme(
    primary = GojekGreen,
    onPrimary = Color.White,
    primaryContainer = GojekGreenContainer,
    onPrimaryContainer = GojekGreenDark,
    secondary = GojekGreenDark,
    onSecondary = Color.White,
    background = Color(0xFFF8F9FA),
    surface = Color.White,
    onBackground = Color(0xFF1F1F1F),
    onSurface = Color(0xFF1F1F1F)
)

@Composable
fun TicketadvanedTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = GojekGreenDark.toArgb()
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}