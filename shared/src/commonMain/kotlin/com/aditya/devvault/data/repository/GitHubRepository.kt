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
        val cached = /* query cache, executeAsOneOrNull */
        val now = /* Clock.System... */

        val isStale = /* your staleness condition — handle null safely */

            if (cached != null && !isStale) {
                // fresh path — decode + return success, no network touch
            }

        return try {
            val fresh = /* call buildGitHubSignal */
                // write to cache via upsertCache, encode to JSON
                Result.success(fresh)
        } catch (e: Exception) {
            // fallback: if cached != null, decode + return success
            // else Result.failure(e)
        }
    }
}