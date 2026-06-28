package com.aditya.devvault.data.remote

import com.aditya.devvault.data.remote.dto.CommitActivityDto
import com.aditya.devvault.data.remote.dto.RepoDto
import com.aditya.devvault.data.remote.dto.UserDto
import com.aditya.devvault.data.repository.toRepoSummary
import com.aditya.devvault.domain.model.GitHubSignal
import com.aditya.devvault.domain.model.LanguageUsage
import com.aditya.devvault.domain.util.calculateConsistencyScore
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.request.*
import kotlinx.coroutines.delay
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant


class GitHubApiClient(private val client: HttpClient) {

    companion object{
        private const val BASE = "https://api.github.com"
    }
    suspend fun fetchUser(username: String): UserDto {
        return client
            .get("$BASE/users/$username")
            .body()
    }

    suspend fun fetchReposList(username: String): List<RepoDto> {
        return client
            .get("$BASE/users/$username/repos?sort=pushed&per_page=50")
            // used query params "repos?sort=pushed&per_page=50" for 50 repos else GitHub will restrict to 30 repos only
            .body()
    }

    suspend fun getLanguagesForRepo(username : String, repo : String): Map<String, Long> {
        return client
            .get("$BASE/repos/$username/$repo/languages")
            .body()
    }

    suspend fun fetchCommitActivity(
        username: String,
        repo: String,
        attempt: Int = 1
    ): List<CommitActivityDto> {

        val response = client.get("$BASE/repos/$username/$repo/stats/commit_activity")

        return when (response.status.value) {
            200 -> {
                response.body()
            }
            202 -> {
                if (attempt >= 3) {
                    throw Exception("GitHub stats not ready after $attempt attempts")
                }
                delay(1500)
                fetchCommitActivity(username, repo, attempt + 1)  // recursion
            }
            else -> {
                throw Exception("GitHub returned ${response.status}")
            }
        }
    }

    suspend fun aggregateLanguages(username: String, repos: List<RepoDto>): List<LanguageUsage> {
        val top10 = repos.sortedByDescending { it.pushedAt }.take(10)
        val totals = mutableMapOf<String, Long>()

        for (repo in top10) {
            val langs = getLanguagesForRepo(username, repo.name)
            for ((language, bytes) in langs) {
                totals[language] = totals.getOrDefault(language, 0L) + bytes
            }
        }

        val grandTotal = totals.values.sum()
        if (grandTotal == 0L) { //edge case
            return emptyList()
        }

        val languageUsages = totals.map { (language, bytes) ->
            LanguageUsage(
                language = language,
                bytes = bytes,
                percentage = (bytes.toFloat() / grandTotal) * 100
            )
        }

        // <- your 2 lines go here: sort + take(5), then return
        return languageUsages
            .sortedByDescending { it.bytes }
            .take(5)

    }

    suspend fun calculateOverallConsistency(
        username: String,
        repos: List<RepoDto>
    ): Int {

        val top10 = repos
            .sortedByDescending { it.pushedAt }
            .take(10)

        val weeklyTotals = mutableMapOf<Long, Int>()

        for (repo in top10) {

            val activity = fetchCommitActivity(
                username,
                repo.name
            )

            for (weekData in activity) {

                weeklyTotals[weekData.week] =
                    weeklyTotals.getOrDefault(
                        weekData.week,
                        0
                    ) + weekData.total
            }
        }

        val weeklyCommitCounts = weeklyTotals
            .toSortedMap()
            .values
            .toList()

        return calculateConsistencyScore(
            weeklyCommitCounts
        )
    }

    suspend fun buildGitHubSignal(
        username: String
    ): GitHubSignal {
        val repos = fetchReposList(username)
        val topLanguages = aggregateLanguages(
            username,
            repos
        )
        val consistencyScore = calculateOverallConsistency(
            username,
            repos
        )
        val now = Clock.System.now().toEpochMilliseconds()
        val activeRepos = repos.filter { repo ->
            val pushedMillis =
                Instant.parse(repo.pushedAt)
                    .toEpochMilliseconds()
            val daysAgo =
                (now - pushedMillis) /
                        (1000L * 60 * 60 * 24)
            daysAgo <= 30
        }
        val repoSummaries = activeRepos.map {
            it.toRepoSummary()
        }
        val lastPushedDaysAgo = repos
            .maxByOrNull {
                Instant.parse(it.pushedAt)
                    .toEpochMilliseconds()
            }
            ?.let { mostRecentRepo ->
                val pushedMillis =
                    Instant.parse(mostRecentRepo.pushedAt)
                        .toEpochMilliseconds()
                (
                        (now - pushedMillis) /
                                (1000L * 60 * 60 * 24)
                        ).toInt()
            }
        return GitHubSignal(
            username = username,
            topLanguages = topLanguages,
            consistencyScore = consistencyScore,
            activeRepos = repoSummaries,
            activeRepoCountThisMonth = repoSummaries.size,
            lastPushedDaysAgo = lastPushedDaysAgo,
        )
    }
}



