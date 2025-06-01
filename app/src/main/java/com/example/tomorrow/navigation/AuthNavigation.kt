package com.example.tomorrow.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.tomorrow.ui.auth.LoginScreen
import com.example.tomorrow.ui.auth.RegisterScreen
import com.example.tomorrow.ui.home.ProfileScreen
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

object AuthRoutes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val PROFILE = "profile"
}

@Composable
fun AuthNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = AuthRoutes.LOGIN
    ) {
        composable(AuthRoutes.LOGIN) {
            LoginScreen(
                onNavigateToRegister = { navController.navigate(AuthRoutes.REGISTER) },
                onLoginSuccess = {
                    navController.navigate(AuthRoutes.PROFILE) {
                        popUpTo(AuthRoutes.LOGIN) { inclusive = true }
                    }
                }
            )
        }
        composable(AuthRoutes.REGISTER) {
            RegisterScreen(
                onBackClick = { navController.popBackStack() },
                onRegisterSuccess = {
                    navController.navigate(AuthRoutes.PROFILE) {
                        popUpTo(AuthRoutes.LOGIN) { inclusive = true }
                    }
                }
            )
        }
        composable(AuthRoutes.PROFILE) {
            ProfileScreen(
                onBackClick = { navController.popBackStack() },
                onLogout = {
                    Firebase.auth.signOut()
                    navController.navigate(AuthRoutes.LOGIN) {
                        popUpTo(0)
                    }
                }
            )
        }
    }
}