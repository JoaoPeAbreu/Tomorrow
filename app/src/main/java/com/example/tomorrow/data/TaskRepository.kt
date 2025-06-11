package com.example.tomorrow.data

import kotlinx.coroutines.flow.Flow

class TaskRepository(
    private val taskDao: TaskDao,
    private val subTaskDao: SubTaskDao
) {

    fun getTasksForUser(userId: String): Flow<List<Task>> {
        return taskDao.getTasksByUser(userId)
    }

    suspend fun addTask(task: Task) {
        taskDao.insertTask(task)
    }

    suspend fun updateTask(task: Task) {
        taskDao.updateTask(task)
    }

    suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task)
    }

    suspend fun getTaskById(id: String): Task? {
        return taskDao.getTaskById(id)
    }

    fun getTasksOrderedByCompletion(userId: String): Flow<List<Task>> {
        return taskDao.getTasksOrderedByCompletion(userId)
    }

    fun getSubTasksForTask(taskId: String): Flow<List<SubTask>> =
        subTaskDao.getSubTasksByTaskId(taskId)

    suspend fun addSubTask(subTask: SubTask) =
        subTaskDao.insertSubTask(subTask)

    suspend fun updateSubTask(subTask: SubTask) =
        subTaskDao.updateSubTask(subTask)

    suspend fun deleteSubTask(subTask: SubTask) =
        subTaskDao.deleteSubTask(subTask)
}

