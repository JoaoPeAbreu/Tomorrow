package com.example.tomorrow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.example.tomorrow.data.TaskRepository
import com.example.tomorrow.data.UserDatabase
import com.example.tomorrow.ui.tasks.TaskCreateScreen
import com.example.tomorrow.ui.tasks.TaskListScreen
import com.example.tomorrow.ui.tasks.TaskViewModel
import com.example.tomorrow.ui.theme.TomorrowTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = UserDatabase.getInstance(applicationContext)
        val repository = TaskRepository(db.taskDao())
        val viewModel = TaskViewModel(repository)

        setContent {
            TomorrowTheme {
                // ✅ Isso está dentro do escopo composable
                var showTaskListScreen by remember { mutableStateOf(false) }

                if (showTaskListScreen) {
                    TaskListScreen(viewModel = viewModel)
                } else {
                    TaskCreateScreen(
                        viewModel = viewModel,
                        onTaskCreated = {
                            showTaskListScreen = true
                        }
                    )
                }
            }
        }
    }
}
