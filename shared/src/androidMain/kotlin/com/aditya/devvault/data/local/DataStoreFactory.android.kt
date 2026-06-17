package com.aditya.devvault.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath

lateinit var appContext: Context

actual fun createDataStore(): DataStore<Preferences> =
    PreferenceDataStoreFactory.createWithPath {
        appContext.filesDir.resolve("devvault.preferences_pb")
            .absolutePath.toPath()
    }