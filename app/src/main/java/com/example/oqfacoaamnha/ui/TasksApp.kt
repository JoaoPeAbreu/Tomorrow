package com.example.oqfacoaamnha.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.oqfacoaamnha.ui.tasksapp.LoginScreen
import com.example.oqfacoaamnha.ui.tasksapp.RegisterScreen
import com.example.oqfacoaamnha.ui.theme.OqFacoAamnhaTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

sealed class AuthScreen(val route: String) {
    data object Login : AuthScreen("login")
    data object Register : AuthScreen("register")
    data object Home : AuthScreen("home")
}

@Composable
fun TasksApp() {
    val navController = rememberNavController()
    val auth: FirebaseAuth = Firebase.auth
    var isUserLoggedIn by remember { mutableStateOf(auth.currentUser != null) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        AppNavigation(
            navController = navController,
            isUserLoggedIn = isUserLoggedIn,
            onAuthSuccess = { isUserLoggedIn = true },
            onLogout = {
                auth.signOut()
                isUserLoggedIn = false
            }
        )
    }
}

@Composable
fun AppNavigation(
    navController: NavHostController,
    isUserLoggedIn: Boolean,
    onAuthSuccess: () -> Unit,
    onLogout: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = if (isUserLoggedIn) AuthScreen.Home.route else AuthScreen.Login.route
    ) {
        composable(AuthScreen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    onAuthSuccess()
                    navController.navigate(AuthScreen.Home.route) {
                        popUpTo(AuthScreen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(AuthScreen.Register.route)
                }
            )
        }
        composable(AuthScreen.Register.route) {
            RegisterScreen(
                onRegisterSuccess = {
                    onAuthSuccess()
                    navController.navigate(AuthScreen.Home.route) {
                        popUpTo(AuthScreen.Login.route) { inclusive = true }
                    }
                },
                onBackToLogin = {
                    navController.popBackStack()
                }
            )
        }
        composable(AuthScreen.Home.route) {
            HomeScreen(
                onLogout = {
                    onLogout()
                    navController.navigate(AuthScreen.Login.route) {
                        popUpTo(0)
                    }
                }
            )
        }
    }
}

@Composable
fun HomeScreen(onLogout: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text("Bem-vindx ao App!")
        Button(onClick = onLogout) {
            Text("Sair")
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    OqFacoAamnhaTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            LoginScreen(
                onLoginSuccess = {},
                onNavigateToRegister = {}
            )
        }
    }
}