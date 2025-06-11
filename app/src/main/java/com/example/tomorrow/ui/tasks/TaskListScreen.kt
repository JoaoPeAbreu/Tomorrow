package com.example.tomorrow.ui.tasks

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tomorrow.data.Task
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(
    viewModel: TaskViewModel,
    onCreateTaskClick: () -> Unit,
    onTaskClick: (Task) -> Unit = {},
    onProfileClick: () -> Unit
) {
    val tasks by viewModel.tasks.collectAsState()

    var priorityFilter by remember { mutableStateOf<Int?>(null) }
    var statusFilter by remember { mutableStateOf<Int?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Minhas Tarefas", style = MaterialTheme.typography.headlineMedium)
            IconButton(onClick = onProfileClick) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Perfil"
                )
            }
        }
        FilterSection(
            priorityFilter = priorityFilter,
            onPriorityChange = {
                priorityFilter = it
                viewModel.setPriorityFilter(it)
            },
            statusFilter = statusFilter,
            onStatusChange = {
                statusFilter = it
                viewModel.setStatusFilter(it)
            },
            onClearFilters = {
                priorityFilter = null
                statusFilter = null
                viewModel.clearFilters()
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onCreateTaskClick,
            modifier = Modifier
                .align(Alignment.End)
                .padding(bottom = 8.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Nova Tarefa")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Criar Tarefa")
        }

        if (tasks.isEmpty()) {
            Text("Nenhuma tarefa encontrada", style = MaterialTheme.typography.bodyLarge)
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(tasks) { task ->
                    val subtasks by viewModel.getSubTasksForTask(task.id).collectAsState(initial = emptyList())

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        task.deadlineMillis?.let { deadlineMillis ->
                            val now = System.currentTimeMillis()
                            val isExpired = deadlineMillis < now
                            val formattedDeadline = remember(deadlineMillis) {
                                SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                                    .format(Date(deadlineMillis))
                            }
                            Text(
                                text = "Data limite: $formattedDeadline" + if (isExpired) " (expirada)" else "",
                                style = MaterialTheme.typography.bodySmall,
                                color = if (isExpired) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                        }

                        TaskItemEditable(
                            task = task,
                            subtasks = subtasks,
                            onTaskUpdate = { updatedTask -> viewModel.updateTask(updatedTask) },
                            onTaskDelete = { taskToDelete -> viewModel.deleteTask(taskToDelete) },
                            onSubTaskUpdate = { updatedSubTask -> viewModel.updateSubTask(updatedSubTask) },
                            onSubTaskCreate = { newSubTask -> viewModel.addSubTask(newSubTask) },
                            onSubTaskDelete = { subTaskToDelete -> viewModel.deleteSubTask(subTaskToDelete) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FilterSection(
    priorityFilter: Int?,
    onPriorityChange: (Int?) -> Unit,
    statusFilter: Int?,
    onStatusChange: (Int?) -> Unit,
    onClearFilters: () -> Unit
) {
    Column {
        Text("Filtros", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DropdownFilter(
                label = "Prioridade",
                options = listOf(
                    null to "Todas",
                    1 to "Baixa",
                    2 to "Média",
                    3 to "Alta"
                ),
                selectedOption = priorityFilter,
                onOptionSelected = onPriorityChange
            )

            DropdownFilter(
                label = "Status",
                options = listOf(
                    null to "Todos",
                    0 to "Para fazer",
                    1 to "Fazendo",
                    2 to "Feita"
                ),
                selectedOption = statusFilter,
                onOptionSelected = onStatusChange
            )

            Button(onClick = onClearFilters) {
                Text("Limpar")
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownFilter(
    label: String,
    options: List<Pair<Int?, String>>,
    selectedOption: Int?,
    onOptionSelected: (Int?) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            readOnly = true,
            value = options.firstOrNull { it.first == selectedOption }?.second ?: "",
            onValueChange = {},
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { (value, optionLabel) ->
                DropdownMenuItem(
                    text = { Text(optionLabel) },
                    onClick = {
                        onOptionSelected(value)
                        expanded = false
                    }
                )
            }
        }
    }
}

