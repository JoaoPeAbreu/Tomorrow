package com.example.tomorrow.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.tomorrow.ui.auth.LoginScreen
import com.example.tomorrow.ui.auth.RegisterScreen
import com.example.tomorrow.ui.home.ProfileScreen
import com.example.tomorrow.ui.tasks.TaskCreateScreen
import com.example.tomorrow.ui.tasks.TaskListScreen
import com.example.tomorrow.ui.tasks.TaskViewModel
import com.example.tomorrow.ui.theme.ThemeViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

object AuthRoutes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val PROFILE = "profile"
    const val TASK_LIST = "taskList"
    const val TASK_CREATE = "taskCreate"
}

@Composable
fun AuthNavigation(
    navController: NavHostController,
    taskViewModel: TaskViewModel,
    themeViewModel: ThemeViewModel
) {
    NavHost(
        navController = navController,
        startDestination = AuthRoutes.LOGIN
    ) {
        composable(AuthRoutes.LOGIN) {
            LoginScreen(
                onNavigateToRegister = { navController.navigate(AuthRoutes.REGISTER) },
                onLoginSuccess = {
                    navController.navigate(AuthRoutes.TASK_LIST) {
                        popUpTo(AuthRoutes.LOGIN) { inclusive = true }
                    }
                }
            )
        }
        composable(AuthRoutes.REGISTER) {
            RegisterScreen(
                onBackClick = { navController.popBackStack() },
                onRegisterSuccess = {
                    navController.navigate(AuthRoutes.LOGIN) {
                        popUpTo(0)
                    }
                }
            )
        }
        composable(AuthRoutes.PROFILE) {
            ProfileScreen(
                themeViewModel = themeViewModel,
                onBackClick = { navController.popBackStack() },
                onLogout = {
                    Firebase.auth.signOut()
                    navController.navigate(AuthRoutes.LOGIN) {
                        popUpTo(0)
                    }
                }
            )
        }
        composable(AuthRoutes.TASK_LIST) {
            TaskListScreen(
                viewModel = taskViewModel,
                onCreateTaskClick = { navController.navigate(AuthRoutes.TASK_CREATE) },
                onProfileClick = { navController.navigate(AuthRoutes.PROFILE) }
            )
        }

        composable(AuthRoutes.TASK_CREATE) {
            TaskCreateScreen(
                viewModel = taskViewModel,
                onTaskCreated = { navController.popBackStack() },
                onCancel = { navController.popBackStack() }
            )
        }
    }
}
