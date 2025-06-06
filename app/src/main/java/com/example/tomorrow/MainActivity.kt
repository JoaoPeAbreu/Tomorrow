package com.example.tomorrow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import com.example.tomorrow.data.TaskRepository
import com.example.tomorrow.data.UserDatabase
import com.example.tomorrow.ui.tasks.TaskCreateScreen
import com.example.tomorrow.ui.tasks.TaskListScreen
import com.example.tomorrow.ui.tasks.TaskViewModel
import com.example.tomorrow.ui.tasks.TaskViewModelFactory
import com.example.tomorrow.ui.theme.TomorrowTheme
import androidx.compose.runtime.*


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = UserDatabase.getInstance(applicationContext)
        val repository = TaskRepository(db.taskDao(), db.subTaskDao())
        val viewModel = TaskViewModel(repository)

        setContent {
            TomorrowTheme {
                var showTaskListScreen by remember { mutableStateOf(true) }

                if (showTaskListScreen) {
                    TaskListScreen(
                        viewModel = viewModel,
                        onCreateTaskClick = { showTaskListScreen = false },
                        onTaskClick = { /* opcional: abrir detalhes da tarefa */ }
                    )
                } else {
                    TaskCreateScreen(
                        viewModel = viewModel,
                        onTaskCreated = { showTaskListScreen = true },
                        onCancel = { showTaskListScreen = true }
                    )
                }
            }
        }
    }
}
