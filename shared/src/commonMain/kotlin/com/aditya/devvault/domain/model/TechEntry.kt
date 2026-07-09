package com.aditya.devvault.domain.model

data class TechEntry(
    val id: Long,
    val name: String,
    val status: TechStatus,
    val firstUsedMonthYear: String,   // "2024-03"
    val lastUsedMonthYear: String?
)

enum class TechStatus { COMFORTABLE, LEARNING, SHIPPED_WITH, DROPPED }