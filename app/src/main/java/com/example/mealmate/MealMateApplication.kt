package com.example.mealmate

import android.app.Application
import com.example.mealmate.data.AppDatabase
import com.google.firebase.FirebaseApp

class MealMateApplication : Application() {
    // Using lazy so the database is only created when needed
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }

    override fun onCreate() {
        super.onCreate()
        // Initialize Firebase
        FirebaseApp.initializeApp(this)
    }
}