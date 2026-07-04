package com.aditya.devvault.presentation.projects

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aditya.devvault.data.repository.ProjectRepository
import com.aditya.devvault.domain.model.ProjectStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class ProjectsViewModel(
    private val projectRepository: ProjectRepository
) : ViewModel() {

    private val _selectedFilter = MutableStateFlow(ProjectFilter.ALL)
    val selectedFilter: StateFlow<ProjectFilter> = _selectedFilter

    val uiState: StateFlow<ProjectsUiState> = combine(
        projectRepository.getAllProjects(),
        _selectedFilter
    ) { allProjects, filter ->

        if (allProjects.isEmpty()) {
            ProjectsUiState.Empty
        } else {
            val filteredProjects = when (filter) {
                ProjectFilter.ALL -> allProjects
                ProjectFilter.BUILDING ->
                    allProjects.filter { it.status == ProjectStatus.BUILDING }

                ProjectFilter.SHIPPED ->
                    allProjects.filter { it.status == ProjectStatus.SHIPPED }

                ProjectFilter.PAUSED ->
                    allProjects.filter { it.status == ProjectStatus.PAUSED }

                ProjectFilter.ABANDONED ->
                    allProjects.filter { it.status == ProjectStatus.ABANDONED }
            }

            ProjectsUiState.Success(
                projects = filteredProjects,
                selectedFilter = filter
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ProjectsUiState.Empty
    )

    fun onFilterSelected(filter: ProjectFilter) {
        _selectedFilter.value = filter
    }
}