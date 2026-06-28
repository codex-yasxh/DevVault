package com.aditya.devvault.data.repository

import com.aditya.devvault.data.local.DevVaultDatabase
import com.aditya.devvault.data.remote.GitHubApiClient
import com.aditya.devvault.domain.model.GitHubSignal
import kotlinx.datetime.Clock
import kotlinx.serialization.json.Json
// + your db, api, domain imports

class GitHubRepository(
    private val api: GitHubApiClient,
    private val db: DevVaultDatabase
) {
    suspend fun getSignal(username: String): Result<GitHubSignal> {
        val cached = db.githubCacheQueries.getCachedSignal(username).executeAsOneOrNull()
        val now = Clock.System.now().toEpochMilliseconds()


//        Condition A: there's no cache row, means fresh → cached == null
//        Condition B: there IS a row, but it's too old → now - cached.fetchedAt > 3_600_000L
        val isStale = cached == null || (now - cached.fetchedAt > 3_600_000L)

        // ---------- Fresh cache ----------
            if (cached != null && !isStale) { //it's redundant in the place, but kotlin's smart-cast handles this
                // fresh path — decode + return success, no network touch
                val cacheDto = Json.decodeFromString<GitHubSignalCacheDto>(
                    cached.signalJson
                )
                return Result.success(cacheDto.toDomain())
            }

        return try {

            val fresh = api.buildGitHubSignal(username)

            val cacheDto = fresh.toCacheDto(
                fetchedAt = now
            )

            db.githubCacheQueries.upsertCache(
                username = username,
                signalJson = Json.encodeToString(cacheDto),
                fetchedAt = now
            )

            Result.success(fresh)

        } catch (e: Exception) {
            if (cached != null) {
                val cachedDto = Json.decodeFromString<GitHubSignalCacheDto>(cached.signalJson)
                Result.success(cachedDto.toDomain())
            } else {
                Result.failure(e)
            }
        }
        }
}