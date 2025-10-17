package com.unimarket.app

import android.app.Application
import com.google.firebase.FirebaseApp

class UniMarketApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}