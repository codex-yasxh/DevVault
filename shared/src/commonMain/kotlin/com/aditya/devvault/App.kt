package com.aditya.devvault

import androidx.compose.runtime.*
import com.aditya.devvault.di.appModule
import org.koin.compose.KoinApplication


@Composable
fun App() {
    KoinApplication(application = {
        modules(appModule)
    }) {
        // your UI
    }
}