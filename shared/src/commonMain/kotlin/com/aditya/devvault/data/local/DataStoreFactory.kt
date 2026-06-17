package com.aditya.devvault.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey

expect fun createDataStore(): DataStore<Preferences>

val USER_NAME_KEY = stringPreferencesKey("user_name")
val GITHUB_USERNAME_KEY = stringPreferencesKey("github_username")

