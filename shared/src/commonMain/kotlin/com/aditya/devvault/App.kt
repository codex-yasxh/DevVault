package com.aditya.devvault

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.aditya.devvault.presentation.github.GitHubScreen
import com.aditya.devvault.presentation.github.GitHubViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App() {
    MaterialTheme {
        val githubViewModel = koinViewModel<GitHubViewModel>()
        GitHubScreen(viewModel = githubViewModel)
    }
}
