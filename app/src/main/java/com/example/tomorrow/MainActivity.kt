package com.example.tomorrow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.tomorrow.navigation.AuthNavigation
import com.example.tomorrow.ui.theme.ThemeViewModel
import com.example.tomorrow.ui.theme.TomorrowTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeViewModel: ThemeViewModel = viewModel()
            themeViewModel.InitializeTheme()
            TomorrowTheme(
                theme = themeViewModel.currentTheme,
                dynamicColor = true
            ) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AuthNavigation(
                        navController = rememberNavController(),
                        themeViewModel = themeViewModel
                    )
                }
            }
        }
    }
}