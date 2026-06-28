package com.aditya.devvault

import android.app.Application
import com.aditya.devvault.data.local.appContext
import com.aditya.devvault.di.appModule
import com.aditya.devvault.di.platformModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module
import com.aditya.devvault.BuildConfig

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // Initialize global appContext for DataStore
        appContext = this
        
        startKoin {
            androidContext(this@MainApplication)
            modules(
                appModule, 
                platformModule,
                module {
                    single<String>(named("GITHUB_TOKEN")) { BuildConfig.GITHUB_PAT }
                }
            )
        }
    }
}

