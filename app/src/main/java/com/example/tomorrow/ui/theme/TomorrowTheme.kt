// Arquivo: com.example.tomorrow.ui.theme.TomorrowTheme.kt
package com.example.tomorrow.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val TomorrowColorScheme = lightColorScheme(
    primary = androidx.compose.ui.graphics.Color(0xFF6200EE),
    secondary = androidx.compose.ui.graphics.Color(0xFF03DAC6),
    tertiary = androidx.compose.ui.graphics.Color(0xFF018786)
)

@Composable
fun TomorrowTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = TomorrowColorScheme,
        content = content
    )
}