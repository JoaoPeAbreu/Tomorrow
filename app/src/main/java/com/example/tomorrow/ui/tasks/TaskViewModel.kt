package com.example.tomorrow.ui.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tomorrow.data.Task
import com.example.tomorrow.data.TaskRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TaskViewModel(private val repository: TaskRepository) : ViewModel() {

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

    private val _priorityFilter = MutableStateFlow<Int?>(null)
    private val _statusFilter = MutableStateFlow<Int?>(null)

    init {
        observeTasks()
    }

    private fun observeTasks() {
        viewModelScope.launch {
            repository.getTasksForUser("mock-user")
                .combine(_priorityFilter) { tasks, priority ->
                    priority?.let { p -> tasks.filter { it.priority == p } } ?: tasks
                }
                .combine(_statusFilter) { tasks, status ->
                    status?.let { s -> tasks.filter { it.status == s } } ?: tasks
                }
                .collect { filteredTasks ->
                    _tasks.value = filteredTasks
                }
        }
    }

    fun setPriorityFilter(priority: Int?) {
        _priorityFilter.value = priority
    }

    fun setStatusFilter(status: Int?) {
        _statusFilter.value = status
    }

    fun clearFilters() {
        _priorityFilter.value = null
        _statusFilter.value = null
    }

    fun updateTask(updatedTask: Task) {
        viewModelScope.launch {
            repository.updateTask(updatedTask) // Atualiza no repositório (banco, API etc)

            // Atualiza a lista local, substituindo a tarefa antiga pela atualizada
            val currentList = _tasks.value.toMutableList()
            val index = currentList.indexOfFirst { it.id == updatedTask.id }
            if (index != -1) {
                currentList[index] = updatedTask
                _tasks.value = currentList
            }
        }
    }

    fun addTask(newTask: Task) {
        viewModelScope.launch {
            repository.addTask(newTask)
            // Opcional: atualizar _tasks localmente para refletir a mudança imediata
            val currentList = _tasks.value.toMutableList()
            currentList.add(newTask)
            _tasks.value = currentList
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repository.deleteTask(task)
            _tasks.value = _tasks.value.filter { it.id != task.id }
        }
    }



}
