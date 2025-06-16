package com.example.tomorrow.ui.tasks

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.tomorrow.data.SubTask
import com.example.tomorrow.data.Task
import java.util.*

@Composable
fun TaskItemEditable(
    task: Task,
    subtasks: List<SubTask>,
    onTaskUpdate: (Task) -> Unit,
    onTaskDelete: (Task) -> Unit,
    onSubTaskUpdate: (SubTask) -> Unit,
    onSubTaskCreate: (SubTask) -> Unit,
    onSubTaskDelete: (SubTask) -> Unit
) {
    var isEditing by remember { mutableStateOf(false) }
    var editedTitle by remember { mutableStateOf(task.title) }
    var editedDescription by remember { mutableStateOf(task.description) }
    var editedPriority by remember { mutableStateOf(task.priority) }
    var editedStatus by remember { mutableStateOf(task.status) }
    var editedDeadline by remember { mutableStateOf(task.deadlineMillis) }

    val context = LocalContext.current
    val calendar = Calendar.getInstance()

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
                        val updatedTask = if (checked) {
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
                        } else {
                            task.copy(
                                status = 0,
                                startedAtMillis = null,
                                completedAtMillis = null,
                                isPaused = false,
                                totalDurationMillis = 0L
                            )
                        }
                        onTaskUpdate(updatedTask)
                    }
                )

                Spacer(modifier = Modifier.width(8.dp))

                if (isEditing) {
                    Column(modifier = Modifier.weight(1f)) {
                        TextField(
                            value = editedTitle,
                            onValueChange = { editedTitle = it },
                            label = { Text("Título") },
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        TextField(
                            value = editedDescription,
                            onValueChange = { editedDescription = it },
                            label = { Text("Descrição") },
                            maxLines = 3
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text("Prioridade:")
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf(1 to "Baixa", 2 to "Média", 3 to "Alta").forEach { (value, label) ->
                                FilterChip(
                                    selected = editedPriority == value,
                                    onClick = { editedPriority = value },
                                    label = { Text(label) }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text("Status:")
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf(0 to "Para Fazer", 1 to "Fazendo", 2 to "Feita").forEach { (value, label) ->
                                FilterChip(
                                    selected = editedStatus == value,
                                    onClick = { editedStatus = value },
                                    label = { Text(label) }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = editedDeadline?.let {
                                    val date = Date(it)
                                    android.text.format.DateFormat.format("dd/MM/yyyy", date).toString()
                                } ?: "Sem data limite",
                                modifier = Modifier.weight(1f)
                            )
                            Button(onClick = {
                                val year = calendar.get(Calendar.YEAR)
                                val month = calendar.get(Calendar.MONTH)
                                val day = calendar.get(Calendar.DAY_OF_MONTH)

                                DatePickerDialog(context, { _, y, m, d ->
                                    calendar.set(y, m, d, 23, 59, 59)
                                    calendar.set(Calendar.MILLISECOND, 999)
                                    editedDeadline = calendar.timeInMillis
                                }, year, month, day).show()
                            }) {
                                Text("Data Limite")
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(onClick = {
                            onTaskUpdate(
                                task.copy(
                                    title = editedTitle,
                                    description = editedDescription,
                                    priority = editedPriority,
                                    status = editedStatus,
                                    deadlineMillis = editedDeadline
                                )
                            )
                            isEditing = false
                        }) {
                            Icon(Icons.Default.Check, contentDescription = "Salvar")
                            Text("Salvar")
                        }
                    }
                } else {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = task.title,
                            style = MaterialTheme.typography.titleMedium
                        )
                        if (!task.description.isNullOrBlank()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = task.description,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }

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

            Spacer(modifier = Modifier.height(16.dp))

            SubTaskList(
                taskId = task.id,
                subTasks = subtasks,
                onSubTaskUpdate = onSubTaskUpdate,
                onSubTaskCreate = onSubTaskCreate,
                onSubTaskDelete = onSubTaskDelete
            )
        }
    }
}

@Composable
fun SubTaskItemEditable(
    subTask: SubTask,
    onSubTaskUpdate: (SubTask) -> Unit,
    onSubTaskDelete: (SubTask) -> Unit
) {
    var isEditing by remember { mutableStateOf(false) }
    var editedTitle by remember { mutableStateOf(subTask.title) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Checkbox(
            checked = subTask.isDone,
            onCheckedChange = { checked ->
                onSubTaskUpdate(subTask.copy(isDone = checked))
            }
        )
        Spacer(modifier = Modifier.width(8.dp))

        if (isEditing) {
            TextField(
                value = editedTitle,
                onValueChange = { editedTitle = it },
                modifier = Modifier.weight(1f),
                singleLine = true
            )
            IconButton(onClick = {
                onSubTaskUpdate(subTask.copy(title = editedTitle))
                isEditing = false
            }) {
                Icon(Icons.Default.Check, contentDescription = "Salvar")
            }
        } else {
            Text(
                text = subTask.title,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = { isEditing = true }) {
                Icon(Icons.Default.Edit, contentDescription = "Editar")
            }
        }

        IconButton(onClick = { onSubTaskDelete(subTask) }) {
            Icon(Icons.Default.Delete, contentDescription = "Deletar")
        }
    }
}

@Composable
fun SubTaskList(
    taskId: String,
    subTasks: List<SubTask>,
    onSubTaskUpdate: (SubTask) -> Unit,
    onSubTaskCreate: (SubTask) -> Unit,
    onSubTaskDelete: (SubTask) -> Unit
) {
    var newSubTaskTitle by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text("Subtarefas", style = MaterialTheme.typography.titleMedium)

        subTasks.forEach { subTask ->
            SubTaskItemEditable(
                subTask = subTask,
                onSubTaskUpdate = onSubTaskUpdate,
                onSubTaskDelete = onSubTaskDelete
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            TextField(
                value = newSubTaskTitle,
                onValueChange = { newSubTaskTitle = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Nova subtarefa") },
                singleLine = true
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    if (newSubTaskTitle.isNotBlank()) {
                        val newSubTask = SubTask(
                            taskId = taskId,
                            title = newSubTaskTitle.trim()
                        )
                        onSubTaskCreate(newSubTask)
                        newSubTaskTitle = ""
                    }
                }
            ) {
                Text("Adicionar")
            }
        }
    }
}

fun priorityLabel(priority: Int): String = when(priority) {
    1 -> "Baixa"
    2 -> "Média"
    3 -> "Alta"
    else -> "Desconhecida"
}

fun statusLabel(status: Int): String = when(status) {
    0 -> "Para fazer"
    1 -> "Fazendo"
    2 -> "Feita"
    else -> "Desconhecido"
}

