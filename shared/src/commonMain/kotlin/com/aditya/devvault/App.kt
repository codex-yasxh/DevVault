package com.aditya.devvault

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.aditya.devvault.data.repository.ProjectRepository
import com.aditya.devvault.presentation.github.GitHubScreen
import com.aditya.devvault.presentation.github.GitHubViewModel
import com.aditya.devvault.presentation.projects.ProjectsScreen
import com.aditya.devvault.presentation.projects.ProjectsViewModel
import com.aditya.devvault.presentation.seedDummyProjects
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App() {
    MaterialTheme {
//        val githubViewModel = koinViewModel<GitHubViewModel>()
//        GitHubScreen(viewModel = githubViewModel) //commented for using projects screen temporarily
        val projectsViewModel = koinViewModel<ProjectsViewModel>()
        ProjectsScreen(viewModel = projectsViewModel)
        val projectRepository = koinInject<ProjectRepository>()

        LaunchedEffect(Unit) {
            seedDummyProjects(projectRepository)
        }


    }
}
