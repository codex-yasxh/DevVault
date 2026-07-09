package com.aditya.devvault.data.repository

import com.aditya.devvault.data.local.TechEntity
import com.aditya.devvault.domain.model.TechEntry
import com.aditya.devvault.domain.model.TechStatus

fun TechEntity.toDomain(): TechEntry {
    return TechEntry(
        id = id,
        name = name,
        status = TechStatus.valueOf(status),
        firstUsedMonthYear = firstUsed,
        lastUsedMonthYear = lastUsed
    )
}