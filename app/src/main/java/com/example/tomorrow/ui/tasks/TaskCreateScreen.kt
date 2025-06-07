package com.example.tomorrow.ui.tasks

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tomorrow.data.Task
import java.util.*
import android.app.DatePickerDialog
import androidx.compose.ui.platform.LocalContext
import java.util.Calendar
import androidx.compose.ui.Alignment

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskCreateScreen(
    viewModel: TaskViewModel,
    onTaskCreated: () -> Unit,
    onCancel: () -> Unit,          // callback para voltar/cancelar
    userId: String = "mock-user"
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf(2) } // Média por padrão
    var deadlineMillis by remember { mutableStateOf<Long?>(null) }

    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val isFormValid = title.isNotBlank()

    fun openDatePicker() {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(context, { _, y, m, d ->
            calendar.set(y, m, d, 23, 59, 59)
            calendar.set(Calendar.MILLISECOND, 999)
            deadlineMillis = calendar.timeInMillis
        }, year, month, day).show()
    }

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

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = deadlineMillis?.let {
                    val date = Date(it)
                    android.text.format.DateFormat.format("dd/MM/yyyy", date).toString()
                } ?: "Sem data limite",
                modifier = Modifier.weight(1f)
            )
            Button(onClick = { openDatePicker() }) {
                Text("Selecionar Data Limite")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Botão Voltar
        OutlinedButton(
            onClick = onCancel,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Voltar")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                val newTask = Task(
                    id = UUID.randomUUID().toString(),
                    title = title.trim(),
                    description = description.trim(),
                    priority = priority,
                    status = 0,
                    userId = userId,
                    deadlineMillis = deadlineMillis
                )
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

