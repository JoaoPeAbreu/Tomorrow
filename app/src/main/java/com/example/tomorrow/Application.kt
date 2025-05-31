package com.example.tomorrow

import android.app.Application

class TaskApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppContextHolder.init(this)
    }
}