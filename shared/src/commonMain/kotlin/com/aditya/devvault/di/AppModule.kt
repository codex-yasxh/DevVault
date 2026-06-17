

package com.aditya.devvault.di

import com.aditya.devvault.data.local.createDataStore
import com.aditya.devvault.data.remote.GitHubApiClient

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header

import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json

import kotlinx.serialization.json.Json

import org.koin.dsl.module

val appModule = module {

    single {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }

            install(HttpTimeout) {
                requestTimeoutMillis = 10_000
            }

            defaultRequest {
                header(HttpHeaders.Accept, "application/vnd.github.v3+json")
                header(HttpHeaders.UserAgent, "DevVault-App")
            }
        }
    }

    single {
        GitHubApiClient(get())
    }

    single {
        createDataStore()
    }
}