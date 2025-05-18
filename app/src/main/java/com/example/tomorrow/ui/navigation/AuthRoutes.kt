package com.example.tomorrow.ui.navigation

sealed class AuthRoutes(val route: String) {
    object Login : AuthRoutes("login")
    object Register : AuthRoutes("register")
    object Home : AuthRoutes("home")
}