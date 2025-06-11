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
import com.example.tomorrow.data.TaskRepository
import com.example.tomorrow.data.UserDatabase
import com.example.tomorrow.navigation.AuthNavigation
import com.example.tomorrow.ui.tasks.TaskViewModel
import com.example.tomorrow.ui.tasks.TaskViewModelFactory
import com.example.tomorrow.ui.theme.ThemeViewModel
import com.example.tomorrow.ui.theme.TomorrowTheme
import com.google.firebase.auth.FirebaseAuth


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeViewModel: ThemeViewModel = viewModel()
            val auth = FirebaseAuth.getInstance()
            val db = UserDatabase.getInstance(applicationContext)
            val repository = TaskRepository(db.taskDao(), db.subTaskDao())
            val taskViewModel: TaskViewModel = viewModel(
                factory = TaskViewModelFactory(repository)
            )

            themeViewModel.InitializeTheme()

            TomorrowTheme(
                theme = themeViewModel.currentTheme,
                dynamicColor = true
            ) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AuthNavigation(
                        themeViewModel = themeViewModel,
                        navController = rememberNavController(),
                        taskViewModel = taskViewModel
                    )
                }
            }
        }
    }
}
