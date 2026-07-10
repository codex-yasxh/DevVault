package com.aditya.devvault.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.aditya.devvault.data.local.DevVaultDatabase
import com.aditya.devvault.data.remote.GitHubApiClient
import com.aditya.devvault.domain.model.GitHubSignal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.serialization.json.Json
// + your db, api, domain imports

class GitHubRepository(
    private val api: GitHubApiClient,
    private val db: DevVaultDatabase
) {
    companion object {
        private const val TTL = 3_600_000L // 1 hour in milliseconds
    }

    suspend fun getSignal(username: String): Result<GitHubFetchResult> {
        val cached = db.githubCacheQueries.getCachedSignal(username).executeAsOneOrNull()
        val now = Clock.System.now().toEpochMilliseconds()
        val isExpired = cached == null || (now - cached.fetchedAt > TTL)

        // ---------- Branch 2: cache hit, not expired — short-circuit ----------
        if (cached != null && !isExpired) {
            val cacheDto = Json.decodeFromString<GitHubSignalCacheDto>(cached.signalJson)
            return Result.success(
                GitHubFetchResult(
                    cacheDto.toDomain(),
                    isStale = false
                )
            )
        }

        return try {
            // ---------- Branch 1: fresh fetch succeeds ----------
            val fresh = api.buildGitHubSignal(username)
            val cacheDto = fresh.toCacheDto(fetchedAt = now)

            db.githubCacheQueries.upsertCache(
                username = username,
                signalJson = Json.encodeToString(cacheDto),
                fetchedAt = now
            )

            Result.success(GitHubFetchResult(fresh, isStale = false))

        } catch (e: Exception) {
            if (cached != null) {
                // ---------- Branch 4: fetch failed, stale cache fallback ----------
                val cachedDto = Json.decodeFromString<GitHubSignalCacheDto>(cached.signalJson)
                Result.success(
                    GitHubFetchResult(
                        cachedDto.toDomain(),
                        isStale = true
                    )
                )
            } else {
                // ---------- Branch 3: fetch failed, no cache at all ----------
                Result.failure(e)
            }
        }
    }

    fun getCachedSignalFlow(username: String): Flow<GitHubSignal?> {
        return db.githubCacheQueries.getCachedSignal(username)
            .asFlow()
            .mapToOneOrNull(Dispatchers.Default) // Use Default in commonMain if IO isn't directly available or use a proper provider
            .map { cached ->
                cached?.let {
                    Json.decodeFromString<GitHubSignalCacheDto>(it.signalJson).toDomain()
                }
            }
    }
}