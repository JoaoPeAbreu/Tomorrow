package com.example.tomorrow.data

import android.content.Context

object UserRepositoryProvider {
    @Volatile private var instance: UserRepository? = null

    fun getRepository(context: Context): UserRepository {
        return instance ?: synchronized(this) {
            instance ?: UserRepository(
                UserDatabase.getInstance(context).userDao()
            ).also { instance = it }
        }
    }
}