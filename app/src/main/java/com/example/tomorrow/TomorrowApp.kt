package com.example.tomorrow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tomorrow.ui.auth.LoginScreen
import com.example.tomorrow.ui.auth.RegisterScreen
import com.example.tomorrow.ui.home.HomeScreen
import com.example.tomorrow.ui.navigation.AuthRoutes
import com.example.tomorrow.ui.theme.TomorrowTheme
import com.google.firebase.FirebaseApp

class TomorrowApp : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        setContent {
            TomorrowTheme {
                TomorrowNavigation()
            }
        }
    }
}

@Composable
fun TomorrowNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AuthRoutes.Login.route
    ) {
        composable(AuthRoutes.Login.route) {
            LoginScreen(navController = navController)
        }
        composable(AuthRoutes.Register.route) {
            RegisterScreen()
        }
        composable(AuthRoutes.Home.route) {
            HomeScreen()
        }
    }
}