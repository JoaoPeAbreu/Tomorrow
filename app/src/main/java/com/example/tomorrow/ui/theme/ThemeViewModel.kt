package com.example.tomorrow.ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ThemeViewModel : ViewModel() {
    var currentTheme by mutableStateOf(ColorTheme.SYSTEM)
        private set

    fun toggleTheme() {
        currentTheme = when (currentTheme) {
            ColorTheme.SYSTEM -> ColorTheme.LIGHT
            ColorTheme.LIGHT -> ColorTheme.DARK
            ColorTheme.DARK -> ColorTheme.SYSTEM
        }
    }
}