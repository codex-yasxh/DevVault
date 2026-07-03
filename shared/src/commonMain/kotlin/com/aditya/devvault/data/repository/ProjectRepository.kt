package com.aditya.devvault.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.aditya.devvault.data.local.DevVaultDatabase
import com.aditya.devvault.data.local.ProjectEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

class ProjectRepository(private val db: DevVaultDatabase) {
    fun getAllProjects() : Flow<List<ProjectEntity>> {
        return db.projectQueries.getAllProjects().asFlow().mapToList(Dispatchers.IO)
    }
}