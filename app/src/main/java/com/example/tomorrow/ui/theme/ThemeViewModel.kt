package com.example.tomorrow.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ThemeViewModel : ViewModel() {
    var currentTheme by mutableStateOf(ColorTheme.DARK)
        private set

    @Composable
    fun InitializeTheme() {
        currentTheme = if (isSystemInDarkTheme()) ColorTheme.DARK else ColorTheme.LIGHT
    }

    fun toggleTheme() {
        currentTheme = when (currentTheme) {
            ColorTheme.LIGHT -> ColorTheme.DARK
            ColorTheme.DARK -> ColorTheme.LIGHT
        }
    }
}