package com.aditya.devvault.domain.util

fun calculateConsistencyScore(weeklyCommits: List<Int>): Int {
    val last12Weeks = weeklyCommits.takeLast(12)
    val activeWeeks = last12Weeks.count { it > 0 }
    val avgCommitsPerActiveWeek = if (activeWeeks > 0)
        last12Weeks.sum().toFloat() / activeWeeks else 0f
    val consistency = (activeWeeks / 12f) * 70
    val volume = minOf(avgCommitsPerActiveWeek / 10f, 1f) * 30
    return (consistency + volume).toInt().coerceIn(0, 100)
}