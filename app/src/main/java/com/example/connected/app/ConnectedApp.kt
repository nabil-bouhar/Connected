package com.example.connected.app

import android.app.Application
import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ConnectedApp : Application() {

    override fun onCreate() {
        super.onCreate()

        appContext = applicationContext

        // Initialize Firebase Auth
        auth = Firebase.auth
    }

    companion object {
        lateinit var appContext: Context
            private set

        lateinit var auth: FirebaseAuth
            private set
    }
}