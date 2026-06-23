package com.aditya.devvault.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CommitActivityDto(
    val total : Int,
    val week : Long, //  Unix Timestamp
)