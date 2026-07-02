package com.aditya.devvault.data.repository

import com.aditya.devvault.domain.model.GitHubSignal

data class GitHubFetchResult(
    val signal: GitHubSignal,
    val isStale: Boolean
)