package com.aditya.devvault.presentation.stack

sealed interface StackUiState {

    data object Empty : StackUiState

    data class Success(
        val timeline: List<TechTimelineGroup>
    ) : StackUiState
}