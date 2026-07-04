package com.aditya.devvault.presentation

import com.aditya.devvault.data.repository.ProjectRepository
import com.aditya.devvault.domain.model.Platform
import com.aditya.devvault.domain.model.Project
import com.aditya.devvault.domain.model.ProjectStatus

suspend fun seedDummyProjects(repository: ProjectRepository) {
    repository.insertProject(
        Project(
            id = 0, // auto-increment, DB assigns real id
            name = "DevVault",
            description = "Developer identity app in KMP",
            status = ProjectStatus.BUILDING,
            platforms = listOf(Platform.ANDROID),
            techStack = listOf("KMP", "Compose", "SQLDelight"),
            decisionNote = "Wanted a project memory tool for myself",
            githubUrl = null,
            liveUrl = null,
            startedAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
    )
    repository.insertProject(
        Project(
            id = 0,
            name = "Burnout Tracker",
            description = "Old habit tracker app",
            status = ProjectStatus.SHIPPED,
            platforms = listOf(Platform.ANDROID),
            techStack = listOf("Kotlin", "Room"),
            decisionNote = null,
            githubUrl = null,
            liveUrl = null,
            startedAt = System.currentTimeMillis() - 10_000_000L,
            updatedAt = System.currentTimeMillis() - 5_000_000L
        )
    )
    repository.insertProject(
        Project(
            id = 0,
            name = "Old Portfolio Site",
            description = "First website attempt",
            status = ProjectStatus.ABANDONED,
            platforms = listOf(Platform.WEB),
            techStack = listOf("HTML", "CSS"),
            decisionNote = null,
            githubUrl = null,
            liveUrl = null,
            startedAt = System.currentTimeMillis() - 50_000_000L,
            updatedAt = System.currentTimeMillis() - 40_000_000L
        )
    )
}