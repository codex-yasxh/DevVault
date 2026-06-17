package com.aditya.devvault.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RepoDto (
    val name : String,
    @SerialName("full_name") val fullName : String,
    val description: String?,
    val language: String?,
    @SerialName("pushed_at") val pushedAt: String,
    val fork : Boolean,
)
