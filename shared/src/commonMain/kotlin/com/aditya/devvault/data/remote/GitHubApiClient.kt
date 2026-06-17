package com.aditya.devvault.data.remote

import com.aditya.devvault.data.remote.dto.RepoDto
import com.aditya.devvault.data.remote.dto.UserDto
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json


class GitHubApiClient(private val client: HttpClient) {

    companion object{
        private const val BASE = "https://api.github.com"
    }
    suspend fun fetchUser(username: String): UserDto {
        return client
            .get("https://api.github.com/users/$username")
            .body()
    }

    suspend fun fetchReposList(username: String): List<RepoDto> {
        return client
            .get("https://api.github.com/users/$username/repos?sort=pushed&per_page=50")
            // used query params "repos?sort=pushed&per_page=50" for 50 repos else GitHub will restrict to 30 repos only
            .body()
    }
}

