package com.aditya.devvault.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.aditya.devvault.data.local.DevVaultDatabase
import com.aditya.devvault.data.local.toDomain
import com.aditya.devvault.domain.model.TechEntry
import com.aditya.devvault.domain.model.TechStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class StackRepository(
    private val db: DevVaultDatabase
) {

    fun getAllTech(): Flow<List<TechEntry>> {
        return db.techStackQueries
            .getAllTech()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { techList ->
                techList.map { it.toDomain() }
            }
    }

    suspend fun insertTech(entry: TechEntry) {
        withContext(Dispatchers.IO) {
            db.techStackQueries.insertTech(
                name = entry.name,
                status = entry.status.name,
                firstUsed = entry.firstUsedMonthYear,
                lastUsed = entry.lastUsedMonthYear
            )
        }
    }

    suspend fun updateTechStatus(
        id: Long,
        status: TechStatus
    ) {
        withContext(Dispatchers.IO) {
            db.techStackQueries.updateTechStatus(
                status = status.name,
                id = id
            )
        }
    }

    suspend fun deleteTech(id: Long) {
        withContext(Dispatchers.IO) {
            db.techStackQueries.deleteTech(id)
        }
    }
}