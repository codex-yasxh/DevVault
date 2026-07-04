package com.aditya.devvault.data.local

import com.aditya.devvault.domain.model.Project
import com.aditya.devvault.domain.model.ProjectStatus
import kotlinx.serialization.json.Json

fun ProjectEntity.toDomain(): Project {
    return Project(
        id = id,
        name = name,
        description = description,
        status = ProjectStatus.valueOf(status),
        platforms = Json.decodeFromString(platforms),
        techStack = Json.decodeFromString(techStack),
        decisionNote = decisionNote,
        githubUrl = githubUrl,
        liveUrl = liveUrl,
        startedAt = startedAt,
        updatedAt = updatedAt
    )
}