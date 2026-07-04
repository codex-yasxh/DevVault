package com.aditya.devvault.presentation.projects

import com.aditya.devvault.domain.model.Project

sealed interface ProjectsUiState {
    data object Empty : ProjectsUiState
    data class Success(
        val projects: List<Project>,
        val selectedFilter: ProjectFilter
    ) : ProjectsUiState
}

enum class ProjectFilter {
    ALL,
    BUILDING,
    SHIPPED,
    PAUSED,
    ABANDONED
}