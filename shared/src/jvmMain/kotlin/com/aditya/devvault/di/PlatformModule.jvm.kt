package com.aditya.devvault.di

import com.aditya.devvault.data.local.DatabaseDriverFactory
import org.koin.dsl.module

val jvmPlatformModule = module {
    single { DatabaseDriverFactory() }
}
