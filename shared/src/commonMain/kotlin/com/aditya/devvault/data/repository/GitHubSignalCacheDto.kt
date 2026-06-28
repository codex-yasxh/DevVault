package com.aditya.devvault.data.repository

import com.aditya.devvault.domain.model.GitHubSignal
import com.aditya.devvault.domain.model.LanguageUsage
import com.aditya.devvault.domain.model.RepoSummary
import kotlinx.serialization.Serializable

@Serializable
data class GitHubSignalCacheDto(
    val username: String,
    val consistencyScore: Int,
    val topLanguages: List<LanguageUsageCacheDto>,
    val activeRepos: List<RepoSummaryCacheDto>,
    val lastPushedDaysAgo: Int,
    val activeRepoCountThisMonth: Int,
    val fetchedAt: Long
)

@Serializable
data class LanguageUsageCacheDto(val language: String, val bytes: Long, val percentage: Float)

@Serializable
data class RepoSummaryCacheDto(val name : String, val description: String?, val language: String?, val pushedAt: Long)
fun LanguageUsage.toCacheDto(): LanguageUsageCacheDto =
    LanguageUsageCacheDto(language = language, bytes = bytes, percentage = percentage)

fun LanguageUsageCacheDto.toDomain(): LanguageUsage =
    LanguageUsage(language = language, bytes = bytes, percentage = percentage)

fun RepoSummary.toCacheDto() : RepoSummaryCacheDto =
    RepoSummaryCacheDto(name = name, description = description, language = language, pushedAt = pushedAt)

fun RepoSummaryCacheDto.toDomain() : RepoSummary =
    RepoSummary(name = name, description = description, language = language, pushedAt = pushedAt)

// mapper: domain -> cache dto
fun GitHubSignal.toCacheDto(fetchedAt: Long): GitHubSignalCacheDto {
    return GitHubSignalCacheDto(
        username = username,
        consistencyScore = consistencyScore,
        topLanguages = topLanguages.map { it.toCacheDto() },
        activeRepos = activeRepos.map { it.toCacheDto() },
        lastPushedDaysAgo = lastPushedDaysAgo ?: 0,
        activeRepoCountThisMonth = activeRepoCountThisMonth,
        fetchedAt = fetchedAt
    )
}

// mapper: cache dto -> domain
fun GitHubSignalCacheDto.toDomain(): GitHubSignal {
    return GitHubSignal(
        username = username,
        consistencyScore = consistencyScore,
        topLanguages = topLanguages.map { it.toDomain() },
        activeRepos = activeRepos.map { it.toDomain() },
        lastPushedDaysAgo = lastPushedDaysAgo,
        activeRepoCountThisMonth = activeRepoCountThisMonth,
    )
}

