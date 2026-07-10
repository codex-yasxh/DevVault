package com.aditya.devvault.presentation.projects

import com.aditya.devvault.domain.model.Project

sealed interface ProjectsUiState {
    data object Loading : ProjectsUiState
    data object Empty : ProjectsUiState
    data class Success(
        val projects: List<Project>,
        val selectedFilter: ProjectFilter,
        val isLoading: Boolean = false
    ) : ProjectsUiState
}

enum class ProjectFilter {
    ALL,
    BUILDING,
    SHIPPED,
    PAUSED,
    ABANDONED
}