package com.example.tomorrow

import android.app.Application
import com.google.firebase.FirebaseApp

class TomorrowApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppContextHolder.init(this)
        FirebaseApp.initializeApp(this)
    }
}