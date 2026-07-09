package com.aditya.devvault.presentation.stack

import com.aditya.devvault.domain.model.TechEntry

data class TechTimelineGroup(
    val year: String,
    val technologies: List<TechEntry>
)