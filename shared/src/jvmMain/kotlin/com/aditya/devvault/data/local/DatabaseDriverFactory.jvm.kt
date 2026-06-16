package com.aditya.devvault.data.local

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.aditya.devvault.data.local.DevVaultDatabase

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        val driver = JdbcSqliteDriver("jdbc:sqlite:devvault.db")
        DevVaultDatabase.Schema.create(driver)
        return driver
    }
}
