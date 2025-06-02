package com.example.tomorrow.ui.tasks

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit

import com.example.tomorrow.data.Task  // importante: seu modelo de dados

@Composable
fun TaskItemEditable(
    task: Task,
    onTaskUpdate: (Task) -> Unit
) {
    var isEditing by remember { mutableStateOf(false) }
    var editedTitle by remember { mutableStateOf(task.title) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = task.status == 2,
                    onCheckedChange = { checked ->
                        val updatedTask = task.copy(status = if (checked) 2 else 0)
                        onTaskUpdate(updatedTask)
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))

                if (isEditing) {
                    TextField(
                        value = editedTitle,
                        onValueChange = { editedTitle = it },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        textStyle = MaterialTheme.typography.bodyLarge
                    )
                    IconButton(onClick = {
                        onTaskUpdate(task.copy(title = editedTitle))
                        isEditing = false
                    }) {
                        Icon(imageVector = Icons.Default.Check, contentDescription = "Salvar")
                    }
                } else {
                    Text(
                        text = task.title,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = { isEditing = true }) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Editar")
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text("Prioridade: ${priorityLabel(task.priority)}")
            Text("Status: ${statusLabel(task.status)}")
        }
    }
}
