package com.aditya.devvault.domain.model

data class LanguageUsage(
    val language: String,
    val bytes: Long,
    val percentage: Float
)

data class RepoSummary(
    val name: String,
    val description: String?,
    val language: String?,
    val pushedAt: Long
)

data class GitHubSignal(
    val username: String,
    val consistencyScore: Int,
    val topLanguages: List<LanguageUsage>,
    val activeRepos: List<RepoSummary>,
    val lastPushedDaysAgo: Int?,
    val activeRepoCountThisMonth: Int,
)