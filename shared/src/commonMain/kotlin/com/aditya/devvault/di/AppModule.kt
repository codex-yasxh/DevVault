

package com.aditya.devvault.di

import com.aditya.devvault.data.local.createDataStore
import org.koin.dsl.module

val appModule = module {
    single { createDataStore() }
}