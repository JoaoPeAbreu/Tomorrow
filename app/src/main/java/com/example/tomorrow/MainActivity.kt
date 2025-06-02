package com.example.tomorrow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.tomorrow.ui.tasks.MockTaskListScreen
import com.example.tomorrow.ui.tasks.TaskListScreen
import com.example.tomorrow.ui.theme.TomorrowTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TomorrowTheme {
                MockTaskListScreen()
            }
        }
    }
}
