package com.aditya.devvault.domain.model

import kotlinx.serialization.Serializable

data class Project(
    val id: Long,
    val name: String,
    val description: String,
    val status: ProjectStatus,
    val platforms: List<Platform>,
    val techStack: List<String>,
    val decisionNote: String?,
    val githubUrl: String?,
    val liveUrl: String?,
    val startedAt: Long,
    val updatedAt: Long
)

enum class ProjectStatus { BUILDING, SHIPPED, PAUSED, ABANDONED }

@Serializable
enum class Platform { ANDROID, IOS, DESKTOP, WEB, BACKEND }