package com.aditya.devvault

import android.app.Application
import com.aditya.devvault.data.local.appContext
import com.aditya.devvault.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // Initialize global appContext for DataStore
        appContext = this
        
        startKoin {
            androidContext(this@MainApplication)
            modules(appModule)
        }
    }
}
