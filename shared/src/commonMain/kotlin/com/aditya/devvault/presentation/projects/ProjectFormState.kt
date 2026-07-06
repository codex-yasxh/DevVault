package com.aditya.devvault.presentation.projects

import com.aditya.devvault.domain.model.Platform
import com.aditya.devvault.domain.model.Project
import com.aditya.devvault.domain.model.ProjectStatus

data class ProjectFormState(
    val id: Long? = null,              // null = Add mode, non-null = Edit mode
    val name: String = "",
    val description: String = "",
    val status: ProjectStatus = ProjectStatus.BUILDING,
    val platforms: List<Platform> = emptyList(),
    val techStack: List<String> = emptyList(),
    val decisionNote: String = "",
    val githubUrl: String = "",
    val liveUrl: String = ""
)

fun ProjectFormState.toProject(originalStartedAt: Long? = null): Project {
    val now = System.currentTimeMillis()
    return Project(
        id = id ?: 0L,
        name = name,
        description = description,
        status = status,
        platforms = platforms,
        techStack = techStack,
        decisionNote = decisionNote.ifBlank { null },
        githubUrl = githubUrl.ifBlank { null },
        liveUrl = liveUrl.ifBlank { null },
        startedAt = originalStartedAt ?: now,
        updatedAt = now
    )
}



fun Project.toFormState(): ProjectFormState {
    return ProjectFormState(
    id = id,
    name = name,
    description = description,
    status = status,
    platforms = platforms,
    techStack = techStack,
    decisionNote = decisionNote ?: "",
    githubUrl = githubUrl?: "",
    liveUrl = liveUrl?: ""
    )
}




















