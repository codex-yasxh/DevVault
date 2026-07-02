package com.aditya.devvault.presentation.github

import com.aditya.devvault.domain.model.GitHubSignal

sealed interface GitHubUiState {
    data object Empty : GitHubUiState
    data object Loading : GitHubUiState
    data class Success(
        val data: GitHubSignal,
        val isStale: Boolean = false
    ) : GitHubUiState
    data class Error(val message: String) : GitHubUiState
}