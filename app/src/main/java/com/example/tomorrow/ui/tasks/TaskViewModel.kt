package com.example.tomorrow.ui.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tomorrow.data.SubTask
import com.example.tomorrow.data.Task
import com.example.tomorrow.data.TaskRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TaskViewModel(private val repository: TaskRepository) : ViewModel() {

    // Tasks
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

    // Subtasks agrupadas por taskId
    private val _subTasksMap = MutableStateFlow<Map<String, List<SubTask>>>(emptyMap())
    val subTasksMap: StateFlow<Map<String, List<SubTask>>> = _subTasksMap.asStateFlow()

    // Filtros
    private val _priorityFilter = MutableStateFlow<Int?>(null)
    private val _statusFilter = MutableStateFlow<Int?>(null)

    private val userId = "mock-user" // Ajuste conforme seu fluxo real de usuário

    // Para controlar observações das subtasks e evitar duplicidade
    private val observedTaskIds = mutableSetOf<String>()

    init {
        observeTasksOrderedAndFiltered()
    }

    private fun observeTasksOrderedAndFiltered() {
        viewModelScope.launch {
            combine(
                repository.getTasksOrderedByCompletion(userId),
                _priorityFilter,
                _statusFilter
            ) { tasks, priority, status ->
                tasks.filter { task ->
                    (priority == null || task.priority == priority) &&
                            (status == null || task.status == status)
                }
            }.collect { filteredTasks ->
                _tasks.value = filteredTasks

                // Atualiza observações das subtasks só para novas tasks
                val currentObserved = observedTaskIds.toSet()
                val newTaskIds = filteredTasks.map { it.id }.toSet()

                // Remove observações de tasks que saíram da lista (opcional)
                val removedTaskIds = currentObserved - newTaskIds
                removedTaskIds.forEach { removedId ->
                    observedTaskIds.remove(removedId)
                    val newMap = _subTasksMap.value.toMutableMap()
                    newMap.remove(removedId)
                    _subTasksMap.value = newMap
                }

                // Adiciona observações para tasks novas
                val taskIdsToObserve = newTaskIds - currentObserved
                taskIdsToObserve.forEach { taskId ->
                    observeSubTasksForTask(taskId)
                }
            }
        }
    }

    private fun observeSubTasksForTask(taskId: String) {
        if (observedTaskIds.contains(taskId)) return
        observedTaskIds.add(taskId)

        viewModelScope.launch {
            repository.getSubTasksForTask(taskId).collect { subtasks ->
                val newMap = _subTasksMap.value.toMutableMap()
                newMap[taskId] = subtasks
                _subTasksMap.value = newMap
            }
        }
    }

    // Permite coletar subtasks para uma taskId específica
    fun getSubTasksForTask(taskId: String): StateFlow<List<SubTask>> {
        return subTasksMap.map { map -> map[taskId] ?: emptyList() }
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    }

    // Filtros
    fun setPriorityFilter(priority: Int?) { _priorityFilter.value = priority }
    fun setStatusFilter(status: Int?) { _statusFilter.value = status }
    fun clearFilters() {
        _priorityFilter.value = null
        _statusFilter.value = null
    }

    // Tasks
    fun updateTask(updatedTask: Task) = viewModelScope.launch {
        repository.updateTask(updatedTask)
        val currentList = _tasks.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == updatedTask.id }
        if (index != -1) {
            currentList[index] = updatedTask
            _tasks.value = currentList
        }
    }
    fun addTask(newTask: Task) = viewModelScope.launch {
        repository.addTask(newTask)
        val currentList = _tasks.value.toMutableList()
        currentList.add(newTask)
        _tasks.value = currentList
    }
    fun deleteTask(task: Task) = viewModelScope.launch {
        repository.deleteTask(task)
        _tasks.value = _tasks.value.filter { it.id != task.id }
        val newMap = _subTasksMap.value.toMutableMap()
        newMap.remove(task.id)
        _subTasksMap.value = newMap
        observedTaskIds.remove(task.id)
    }

    // Subtasks
    fun updateSubTask(subTask: SubTask) = viewModelScope.launch {
        repository.updateSubTask(subTask)
        val currentMap = _subTasksMap.value.toMutableMap()
        val list = currentMap[subTask.taskId]?.toMutableList() ?: mutableListOf()
        val index = list.indexOfFirst { it.id == subTask.id }
        if (index != -1) {
            list[index] = subTask
        } else {
            list.add(subTask)
        }
        currentMap[subTask.taskId] = list
        _subTasksMap.value = currentMap
    }
    fun addSubTask(subTask: SubTask) = viewModelScope.launch {
        repository.addSubTask(subTask)
        val currentMap = _subTasksMap.value.toMutableMap()
        val list = currentMap[subTask.taskId]?.toMutableList() ?: mutableListOf()
        list.add(subTask)
        currentMap[subTask.taskId] = list
        _subTasksMap.value = currentMap
    }
    fun deleteSubTask(subTask: SubTask) = viewModelScope.launch {
        repository.deleteSubTask(subTask)
        val currentMap = _subTasksMap.value.toMutableMap()
        val list = currentMap[subTask.taskId]?.toMutableList() ?: mutableListOf()
        list.removeAll { it.id == subTask.id }
        currentMap[subTask.taskId] = list
        _subTasksMap.value = currentMap
    }

}
