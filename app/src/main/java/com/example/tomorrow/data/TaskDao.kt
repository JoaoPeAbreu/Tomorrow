package com.example.tomorrow.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Query("SELECT * FROM tasks WHERE userId = :userId")
    fun getTasksByUser(userId: String): Flow<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("SELECT * FROM tasks WHERE id = :taskId")
    suspend fun getTaskById(taskId: String): Task?

    @Query("""
    SELECT * FROM tasks
    WHERE userId = :userId
    ORDER BY 
        CASE WHEN completedAtMillis IS NULL THEN 1 ELSE 0 END,
        completedAtMillis
    """)
    fun getTasksOrderedByCompletion(userId: String): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE title LIKE '%' || :query || '%'")
    fun searchTasks(query: String): Flow<List<Task>>
}

