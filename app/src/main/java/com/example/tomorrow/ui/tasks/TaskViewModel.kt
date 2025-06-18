package com.example.tomorrow.ui.tasks

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tomorrow.data.SubTask
import com.example.tomorrow.data.Task
import com.example.tomorrow.data.TaskRepository
import com.example.tomorrow.notification.TaskNotificationService
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalCoroutinesApi::class)
class TaskViewModel(private val repository: TaskRepository, private val context: Context) : ViewModel() {

    private val _query = MutableStateFlow("")

    fun setQuery(newQuery: String) {
        _query.value = newQuery
    }

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

    // Subtasks agrupadas por taskId
    private val _subTasksMap = MutableStateFlow<Map<String, List<SubTask>>>(emptyMap())
    val subTasksMap: StateFlow<Map<String, List<SubTask>>> = _subTasksMap.asStateFlow()

    private val _priorityFilter = MutableStateFlow<Int?>(null)
    private val _statusFilter = MutableStateFlow<Int?>(null)

    private val observedTaskIds = mutableSetOf<String>()

    private val auth: FirebaseAuth = Firebase.auth
    private val _currentUserId = MutableStateFlow<String?>(null)
    val currentUserId: StateFlow<String?> = _currentUserId.asStateFlow()

    private val notificationService by lazy { TaskNotificationService(context) }

    init {
        auth.addAuthStateListener { firebaseAuth ->
            _currentUserId.value = firebaseAuth.currentUser?.uid
        }
        viewModelScope.launch {
            combine(
                _currentUserId.filterNotNull(),
                _query,
                _priorityFilter,
                _statusFilter
            ) { userId, query, priority, status ->
                Triple(userId, query, Pair(priority, status))
            }.flatMapLatest { (userId, query, filters) ->
                repository.getTasksOrderedByCompletion(userId).map { tasks ->
                    tasks.filter { task ->
                        (query.isBlank() || task.title.contains(query, ignoreCase = true)) &&
                                (filters.first == null || task.priority == filters.first) &&
                                (filters.second == null || task.status == filters.second)
                    }
                }
            }.collect { filteredTasks ->
                _tasks.value = filteredTasks

                val currentObserved = observedTaskIds.toSet()
                val newTaskIds = filteredTasks.map { it.id }.toSet()

                val removedTaskIds = currentObserved - newTaskIds
                removedTaskIds.forEach { removedId ->
                    observedTaskIds.remove(removedId)
                    val newMap = _subTasksMap.value.toMutableMap()
                    newMap.remove(removedId)
                    _subTasksMap.value = newMap
                }

                val taskIdsToObserve = newTaskIds - currentObserved
                taskIdsToObserve.forEach { taskId ->
                    observeSubTasksForTask(taskId)
                }

                val notifyUsers = filteredTasks
                notifyUsers.forEach {task ->
                    checkDeadlines(task)
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

    fun getSubTasksForTask(taskId: String): StateFlow<List<SubTask>> {
        return subTasksMap.map { map -> map[taskId] ?: emptyList() }
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    }

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
    private fun checkDeadlines(task: Task) {
        if(task.allowNotification) {
            task.deadlineMillis?.let { deadline ->
                val tomorrow = System.currentTimeMillis() + 24 * 60 * 60 * 1000L
                val formattedTomorrow = SimpleDateFormat("dd/MM/yyy").format(Date(tomorrow))
                val formattedDeadline = SimpleDateFormat("dd/MM/yyy").format(Date(deadline))

                if (formattedTomorrow == formattedDeadline) {
                    if (!task.notificationShown) {
                        notificationService.showTaskNotification(task)
                        viewModelScope.launch {
                            repository.updateTask(task.copy(notificationShown = true))
                        }
                    }
                }
            }
        }
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

    fun getUserId(): String {
        return auth.currentUser?.uid ?: ""
    }
}
