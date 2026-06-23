package com.aditya.devvault.data.repository

import com.aditya.devvault.data.remote.dto.RepoDto
import com.aditya.devvault.domain.model.RepoSummary
import kotlinx.datetime.Instant

fun RepoDto.toRepoSummary(): RepoSummary {
    return RepoSummary(
        name = name,
        description = description,
        language = language,
        pushedAt = try {
            Instant.parse(pushedAt).toEpochMilliseconds()
        } catch (e: Exception) {
            0L
        }
    )
}
