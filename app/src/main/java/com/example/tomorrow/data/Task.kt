package com.example.tomorrow.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),

    val title: String,

    val description: String = "",

    val createdAtMillis: Long = System.currentTimeMillis(),
    val startedAtMillis: Long? = null,
    val completedAtMillis: Long? = null,

    val priority: Int = 2, // 1: Baixa, 2: Média, 3: Alta

    val status: Int = 0, // 0: A Fazer, 1: Em Progresso, 2: Concluído

    val userId: String = "",

    val isPaused: Boolean = false,
    val totalDurationMillis: Long = 0L,
    val deadlineMillis: Long? = null
)
