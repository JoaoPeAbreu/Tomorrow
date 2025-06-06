package com.example.tomorrow.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [UserEntity::class, Task::class, SubTask::class], version = 4, exportSchema = false)
@TypeConverters(CastConverter::class)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun taskDao(): TaskDao
    abstract fun subTaskDao(): SubTaskDao


    companion object {
        @Volatile private var INSTANCE: UserDatabase? = null

        fun getInstance(context: Context): UserDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                                context.applicationContext,
                                UserDatabase::class.java,
                                "users.db"
                            ).fallbackToDestructiveMigration(false)
                    .build().also { INSTANCE = it }

            }
        }
    }
}