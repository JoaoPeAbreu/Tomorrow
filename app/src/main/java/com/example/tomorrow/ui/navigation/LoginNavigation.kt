package com.example.tomorrow.ui.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.tomorrow.ui.auth.LoginScreen
import com.example.tomorrow.ui.auth.LoginViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

const val loginRoute: String = "login"

fun NavGraphBuilder.loginScreen(
    onNavigateToTasksList: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    composable(loginRoute) {
        val viewModel = koinViewModel<LoginViewModel>()
        val uiState by viewModel.uiState.collectAsState()
        val scope = rememberCoroutineScope()
        val isAuthenticated by viewModel.isAuthenticated
            .collectAsState(initial = false)
        LaunchedEffect(isAuthenticated) {
            if (isAuthenticated) {
                onNavigateToTasksList()
            }
        }
        LoginScreen(
            uiState = uiState,
            onLoginClick = {
                scope.launch {
                    viewModel.login()
                }
            },
            onRegisterClick = onNavigateToRegister
        )
    }

}

fun NavHostController.navigateToLogin(
    navOptions: NavOptions? = null
) {
    navigate(loginRoute, navOptions)
}