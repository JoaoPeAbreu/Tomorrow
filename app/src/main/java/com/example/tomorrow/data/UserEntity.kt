package com.example.tomorrow.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "users")
@TypeConverters(CastConverter::class)
data class UserEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val email: String,
    val password: String
)