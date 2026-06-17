package com.aditya.devvault.data.remote

import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

val httpClient = HttpClient {
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
        })
    }
    defaultRequest {
        header("Accept", "application/vnd.github.v3+json")
        header("User-Agent", "DevVault-App")
    }
}