package com.aditya.devvault.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath
import java.io.File


actual fun createDataStore(): DataStore<Preferences> {

    val homeDir = System.getProperty("user.home")

    val appDir = File(homeDir, ".devvault")

    if (!appDir.exists()) {
        appDir.mkdirs()
    }

    return PreferenceDataStoreFactory.createWithPath(
        produceFile = {
            File(appDir, "devvault.preferences_pb" ).absolutePath.toPath()
        }
    )

}