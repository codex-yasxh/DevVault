package com.aditya.devvault

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.aditya.devvault.presentation.projects.ProjectsScreen
import com.aditya.devvault.presentation.projects.ProjectsViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App() {
    MaterialTheme {
        val projectsViewModel = koinViewModel<ProjectsViewModel>()
        ProjectsScreen(viewModel = projectsViewModel)
    }
}
