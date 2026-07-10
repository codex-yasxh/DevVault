package com.aditya.devvault.presentation.navigation

// Lightweight sealed-class navigation for a 5-screen flat app.
// No nav library dependency — the app shape (bottom nav + 1 push-on-top)
// doesn't benefit from a graph-based nav system at this scale.
// Tradeoff: deep linking, multi-module nav, or complex nested graphs
// would require switching to Compose Multiplatform Navigation post-MVP.
sealed class Screen {
    data object Home : Screen()
    data object Projects : Screen()
    data object GitHub : Screen()
    data object Stack : Screen()
    data object Settings : Screen()
    data class ProjectDetail(val projectId: Long) : Screen()
}
