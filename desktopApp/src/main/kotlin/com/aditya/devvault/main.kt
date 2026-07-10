package com.aditya.devvault

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.aditya.devvault.di.appModule
import com.aditya.devvault.di.jvmPlatformModule
import org.koin.core.context.startKoin

fun main() = application {
    startKoin {
        modules(appModule, jvmPlatformModule)
    }

    Window(
        onCloseRequest = ::exitApplication,
        title = "DevVault",
    ) {
        App()
    }
}
