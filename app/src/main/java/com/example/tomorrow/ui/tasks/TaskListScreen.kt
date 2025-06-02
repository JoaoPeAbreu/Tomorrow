package com.example.tomorrow.ui.tasks

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tomorrow.data.Task


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(
    viewModel: TaskViewModel = viewModel(),
    onTaskClick: (Task) -> Unit = {}
) {
    val tasks by viewModel.tasks.collectAsState()

    var priorityFilter by remember { mutableStateOf<Int?>(null) }
    var statusFilter by remember { mutableStateOf<Int?>(null) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

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

        if (tasks.isEmpty()) {
            Text("Nenhuma tarefa encontrada", style = MaterialTheme.typography.bodyLarge)
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(tasks) { task ->
                    TaskItemEditable(
                        task = task,
                        onTaskUpdate = { updatedTask -> viewModel.updateTask(updatedTask) }
                        )
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

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DropdownFilter(
                label = "Prioridade",
                options = listOf(null to "Todas", 1 to "Baixa", 2 to "Média", 3 to "Alta"),
                selectedOption = priorityFilter,
                onOptionSelected = onPriorityChange
            )

            DropdownFilter(
                label = "Status",
                options = listOf(null to "Todos", 0 to "A Fazer", 1 to "Em Progresso", 2 to "Concluído"),
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
            modifier = Modifier.menuAnchor()
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskItem(task: Task, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(task.title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Prioridade: ${priorityLabel(task.priority)}")
            Text("Status: ${statusLabel(task.status)}")
        }
    }
}

fun priorityLabel(priority: Int): String = when (priority) {
    1 -> "Baixa"
    2 -> "Média"
    3 -> "Alta"
    else -> "Desconhecida"
}

fun statusLabel(status: Int): String = when (status) {
    0 -> "A Fazer"
    1 -> "Em Progresso"
    2 -> "Concluído"
    else -> "Desconhecido"
}

@Composable
fun MockTaskListScreen() {
    val fakeTasks = listOf(
        Task(
            title = "Estudar Compose",
            description = "Ver os fundamentos de Jetpack Compose",
            priority = 2,
            status = 0
        ),
        Task(
            title = "Trabalhar no projeto",
            description = "Avançar no app de tarefas",
            priority = 3,
            status = 1
        ),
        Task(
            title = "Enviar relatório",
            description = "Relatório de atividades",
            priority = 1,
            status = 2
        )
    )

    LazyColumn {
        items(fakeTasks) { task ->
            TaskItem(task = task, onClick = {})
        }
    }
}

