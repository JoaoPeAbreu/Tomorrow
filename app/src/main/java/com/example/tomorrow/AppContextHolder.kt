package com.example.tomorrow

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

object AppContextHolder {
    @SuppressLint("StaticFieldLeak")
    lateinit var appContext: Context
        private set

    fun init(app: Application) {
        appContext = app.applicationContext
    }
}