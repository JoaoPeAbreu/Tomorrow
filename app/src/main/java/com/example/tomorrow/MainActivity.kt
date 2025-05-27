package com.example.tomorrow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.tomorrow.ui.auth.LoginScreen
import com.example.tomorrow.ui.auth.LoginViewModel
import com.example.tomorrow.ui.theme.TomorrowTheme
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TomorrowTheme {
                val viewModel = koinViewModel<LoginViewModel>()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                val scope = rememberCoroutineScope()

                LoginScreen(
                    uiState = uiState,
                    onLoginClick = {
                        scope.launch {
                            viewModel.login()
                        }
                    },
                    onRegisterClick = { /* Handle register click */ }
                )
            }
        }
    }
}