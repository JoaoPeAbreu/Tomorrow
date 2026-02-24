package com.example.tomorrow.ui.theme

import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

enum class ColorTheme {
    LIGHT, DARK
}

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40
)

val LocalColorTheme = compositionLocalOf { ColorTheme.DARK }

@Composable
fun TomorrowTheme(
    theme: ColorTheme,
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val useDarkTheme = when (theme) {
        ColorTheme.LIGHT -> false
        ColorTheme.DARK -> true
    }

    val colorScheme = remember(useDarkTheme, dynamicColor, context) {
        when {
            dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                if (useDarkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
            }
            useDarkTheme -> DarkColorScheme
            else -> LightColorScheme
        }
    }

    CompositionLocalProvider(LocalColorTheme provides theme) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}

val MaterialTheme.currentTheme: ColorTheme
    @Composable
    get() = LocalColorTheme.current