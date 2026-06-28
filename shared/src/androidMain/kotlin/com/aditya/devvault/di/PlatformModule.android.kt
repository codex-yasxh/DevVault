package com.aditya.devvault.di

import com.aditya.devvault.data.local.DatabaseDriverFactory
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val platformModule = module {
    single { DatabaseDriverFactory(androidContext()) }
}
