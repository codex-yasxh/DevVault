package com.aditya.devvault.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.aditya.devvault.data.local.DevVaultDatabase
import com.aditya.devvault.data.local.ProjectEntity
import com.aditya.devvault.data.local.toDomain
import com.aditya.devvault.domain.model.Project
import com.aditya.devvault.domain.model.ProjectStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ProjectRepository(private val db: DevVaultDatabase) {
    fun getAllProjects(): Flow<List<Project>> {
        return db.projectQueries
            .getAllProjects()
            .asFlow()
            .mapToList(
                Dispatchers.IO
            )
            .map { entities ->
                entities.map { it.toDomain() }
            }
    }

    fun getProjectsByStatus(status: ProjectStatus): Flow<List<ProjectEntity>> {
        return db.projectQueries
            .getProjectsByStatus(status.name)
            .asFlow()
            .mapToList(Dispatchers.IO)
    }

    suspend fun insertProject(project: Project){
        withContext(
            Dispatchers.IO
        ){
            db.projectQueries
                .insertProject(
                    id = project.id,
                    name = project.name,
                    description = project.description,
                    status = project.status.name,
                    platforms = Json.encodeToString(project.platforms),
                    techStack = Json.encodeToString(project.techStack),
                    decisionNote = project.decisionNote,
                    githubUrl = project.githubUrl,
                    liveUrl = project.liveUrl,
                    startedAt = project.startedAt,
                    updatedAt = project.updatedAt
                )
        }
    }

    suspend fun deleteProject(id: Long){
        withContext(Dispatchers.IO){
            db.projectQueries.deleteProject(id)
        }
    }
}