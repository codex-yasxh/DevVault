package com.aditya.devvault.presentation.projects

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aditya.devvault.domain.model.Project
import com.aditya.devvault.domain.model.ProjectStatus
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProjectsScreen(
    viewModel: ProjectsViewModel = koinViewModel(),
    onProjectClick: (Long) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val isSheetVisible by viewModel.isSheetVisible.collectAsState()
    val editingProject by viewModel.editingProject.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = viewModel::onAddProjectClicked) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add project"
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .statusBarsPadding()
        ) {
            when(val state = uiState) {
                is ProjectsUiState.Empty -> EmptyState()
                is ProjectsUiState.Success -> {
                    SuccessState(
                        projects = state.projects,
                        selectedFilter = state.selectedFilter,
                        onFilterSelected = { viewModel.onFilterSelected(it) },
                        onProjectClick = { project -> onProjectClick(project.id) }
                    )
                }
            }
        }
    }

    if (isSheetVisible) {
        AddEditProjectSheet(
            projectToEdit = editingProject,
            onDismiss = { viewModel.onDismissSheet() },
            onSave = { project -> viewModel.onSaveProject(project) }
        )
    }
}

@Composable
private fun EmptyState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "No projects yet",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Tap + to add your first project",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun SuccessState(
    projects: List<Project>,
    selectedFilter: ProjectFilter,
    onFilterSelected: (ProjectFilter) -> Unit,
    onProjectClick: (Project) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        FilterTabRow(
            selectedFilter = selectedFilter,
            onFilterSelected = onFilterSelected
        )

        if (projects.isEmpty()) {
            Box(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No projects in this filter",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(projects, key = { it.id }) { project ->
                    ProjectCard(
                        project = project,
                        onClick = { onProjectClick(project) }
                    )
                }
            }
        }
    }
}

@Composable
private fun FilterTabRow(
    selectedFilter: ProjectFilter,
    onFilterSelected: (ProjectFilter) -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(ProjectFilter.entries) { filter ->
            FilterChip(
                selected = filter == selectedFilter,
                onClick = { onFilterSelected(filter) },
                label = { Text(filter.label()) }
            )
        }
    }
}

@Composable
private fun ProjectCard(
    project: Project,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = project.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                StatusBadge(status = project.status)
            }

            if (project.description.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = project.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (project.techStack.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(project.techStack) { tech ->
                        AssistChip(
                            onClick = {},
                            label = { Text(tech, style = MaterialTheme.typography.labelSmall) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StatusBadge(status: ProjectStatus) {
    val (bgColor, textColor, label) = when (status) {
        ProjectStatus.BUILDING -> Triple(Color(0xFF1E88E5), Color.White, "Building")
        ProjectStatus.SHIPPED -> Triple(Color(0xFF43A047), Color.White, "Shipped")
        ProjectStatus.PAUSED -> Triple(Color(0xFFFB8C00), Color.White, "Paused")
        ProjectStatus.ABANDONED -> Triple(Color(0xFF757575), Color.White, "Abandoned")
    }

    Box(
        modifier = Modifier
            .background(color = bgColor, shape = RoundedCornerShape(50))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = label,
            color = textColor,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Medium
        )
    }
}

private fun ProjectFilter.label(): String = when (this) {
    ProjectFilter.ALL -> "All"
    ProjectFilter.BUILDING -> "Building"
    ProjectFilter.SHIPPED -> "Shipped"
    ProjectFilter.PAUSED -> "Paused"
    ProjectFilter.ABANDONED -> "Abandoned"
}