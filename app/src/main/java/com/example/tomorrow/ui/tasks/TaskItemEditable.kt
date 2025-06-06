package com.example.tomorrow.ui.tasks

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import com.example.tomorrow.data.Task

@Composable
fun TaskItemEditable(
    task: Task,
    onTaskUpdate: (Task) -> Unit,
    onTaskDelete: (Task) -> Unit
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
                        val now = System.currentTimeMillis()
                        val updatedTask = when {
                            checked -> {
                                // Concluir tarefa
                                val elapsed = if (!task.isPaused && task.startedAtMillis != null) {
                                    now - task.startedAtMillis
                                } else 0L
                                task.copy(
                                    status = 2,
                                    startedAtMillis = null,
                                    completedAtMillis = now,
                                    isPaused = false,
                                    totalDurationMillis = task.totalDurationMillis + elapsed
                                )
                            }
                            else -> {
                                // Reabrir tarefa
                                task.copy(
                                    status = 0,
                                    startedAtMillis = null,
                                    completedAtMillis = null,
                                    isPaused = false,
                                    totalDurationMillis = 0L
                                )
                            }
                        }
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

                IconButton(onClick = { onTaskDelete(task) }) {
                    Icon(Icons.Default.Delete, contentDescription = "Deletar")
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text("Prioridade: ${priorityLabel(task.priority)}")
            Text("Status: ${statusLabel(task.status)}")

            // Controles de tempo
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(top = 8.dp)
            ) {
                when (task.status) {
                    0 -> {
                        Button(onClick = {
                            val now = System.currentTimeMillis()
                            onTaskUpdate(task.copy(
                                status = 1,
                                startedAtMillis = now,
                                isPaused = false
                            ))
                        }) {
                            Text("Iniciar")
                        }
                    }

                    1 -> {
                        if (task.isPaused) {
                            Button(onClick = {
                                val now = System.currentTimeMillis()
                                onTaskUpdate(task.copy(
                                    startedAtMillis = now,
                                    isPaused = false
                                ))
                            }) {
                                Text("Retomar")
                            }
                        } else {
                            Button(onClick = {
                                val now = System.currentTimeMillis()
                                val elapsed = now - (task.startedAtMillis ?: now)
                                onTaskUpdate(task.copy(
                                    startedAtMillis = null,
                                    isPaused = true,
                                    totalDurationMillis = task.totalDurationMillis + elapsed
                                ))
                            }) {
                                Text("Pausar")
                            }

                            Button(onClick = {
                                val now = System.currentTimeMillis()
                                val elapsed = now - (task.startedAtMillis ?: now)
                                onTaskUpdate(task.copy(
                                    status = 2,
                                    completedAtMillis = now,
                                    startedAtMillis = null,
                                    isPaused = false,
                                    totalDurationMillis = task.totalDurationMillis + elapsed
                                ))
                            }) {
                                Text("Concluir")
                            }
                        }
                    }

                    2 -> {
                        val totalMinutes = task.totalDurationMillis / 60000
                        Text("Tempo total: $totalMinutes min")
                    }
                }
            }
        }
    }
}
