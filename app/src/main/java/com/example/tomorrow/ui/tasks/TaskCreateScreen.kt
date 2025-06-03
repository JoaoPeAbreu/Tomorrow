package com.example.tomorrow.ui.tasks

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tomorrow.data.Task
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskCreateScreen(
    viewModel: TaskViewModel,
    onTaskCreated: () -> Unit,  // Callback para navegar após salvar
    userId: String = "mock-user" // Passe o userId real se tiver
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf(2) } // Média por padrão

    val isFormValid = title.isNotBlank()

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
    ) {
        Text("Nova Tarefa", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Título") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Descrição") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 4
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text("Prioridade")

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf(1 to "Baixa", 2 to "Média", 3 to "Alta").forEach { (value, label) ->
                FilterChip(
                    selected = priority == value,
                    onClick = { priority = value },
                    label = { Text(label) }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val newTask = Task(
                    id = UUID.randomUUID().toString(),
                    title = title.trim(),
                    description = description.trim(),
                    priority = priority,
                    status = 0, // A Fazer por padrão
                    userId = userId
                )

                Log.d("TaskCreate", "Tarefa criada: $newTask")

                viewModel.addTask(newTask)
                onTaskCreated()
            },
            enabled = isFormValid,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Salvar")
        }
    }
}
