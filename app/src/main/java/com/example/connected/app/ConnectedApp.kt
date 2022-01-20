package com.example.connected.app

import android.app.Application
import android.content.Context
import com.example.connected.di.appModules
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class ConnectedApp : Application() {

    override fun onCreate() {
        super.onCreate()

        appContext = applicationContext

        // Initialize Firebase Auth
        auth = Firebase.auth

        startKoin {
            androidLogger()
            androidContext(this@ConnectedApp)
            modules(appModules)
        }
    }

    companion object {
        lateinit var appContext: Context
            private set

        lateinit var auth: FirebaseAuth
            private set
    }
}