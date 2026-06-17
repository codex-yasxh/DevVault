package com.aditya.devvault.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    //underscore won't work with ktor and camel case doesn't work so using @SerialName instead
    @SerialName("avatar_url") val avatarUrl : String,
    val name : String?,
    val bio : String?,
    @SerialName("public_repos") val publicRepos: Int
)