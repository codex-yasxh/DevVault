package com.aditya.devvault

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aditya.devvault.data.repository.GitHubRepository
import com.aditya.devvault.presentation.github.GitHubScreen
import com.aditya.devvault.presentation.github.GitHubViewModel
import org.koin.compose.koinInject

@Composable
fun App() {
    MaterialTheme {
        val repository = koinInject<GitHubRepository>()
        val prefs = koinInject<DataStore<Preferences>>()

        val githubViewModel: GitHubViewModel = viewModel {
            GitHubViewModel(repository, prefs)
        }

        GitHubScreen(viewModel = githubViewModel)
    }
}
