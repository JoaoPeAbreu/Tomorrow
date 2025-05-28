package com.example.tomorrow.data

import kotlinx.coroutines.flow.Flow

class TaskRepository(private val taskDao: TaskDao) {

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
}
