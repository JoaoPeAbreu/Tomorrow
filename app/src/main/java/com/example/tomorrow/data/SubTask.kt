package com.example.tomorrow.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "subtasks")
data class SubTask(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),

    val taskId: String, // ID da task pai

    val title: String,

    val isDone: Boolean = false
)
