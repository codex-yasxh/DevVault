package com.aditya.devvault.presentation.home

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aditya.devvault.data.local.GITHUB_USERNAME_KEY
import com.aditya.devvault.data.local.USER_NAME_KEY
import com.aditya.devvault.data.repository.GitHubRepository
import com.aditya.devvault.data.repository.ProjectRepository
import com.aditya.devvault.data.repository.StackRepository
import com.aditya.devvault.domain.model.GitHubSignal
import com.aditya.devvault.domain.model.Project
import com.aditya.devvault.domain.model.ProjectStatus
import com.aditya.devvault.domain.model.TechEntry
import com.aditya.devvault.domain.model.TechStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class HomeUiState(
    val developerType: String = "Loading...",
    val activeProjects: List<Project> = emptyList(),
    val pulseText: String = "",
    val isLoading: Boolean = true
)

class HomeViewModel(
    private val projectRepository: ProjectRepository,
    private val gitHubRepository: GitHubRepository,
    private val stackRepository: StackRepository,
    private val prefs: DataStore<Preferences>
) : ViewModel() {

    private val _activeUsername = MutableStateFlow("")

    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName.asStateFlow()

    // Using getCachedSignalFlow() via flatMapLatest because it already exposes a reactive
    // SQLDelight Flow — no need for a manual MutableStateFlow wrapper.
    private val signalFlow = _activeUsername.flatMapLatest { username ->
        if (username.isBlank()) flowOf(null)
        else gitHubRepository.getCachedSignalFlow(username)
    }

    init {
        viewModelScope.launch {
            val savedName = prefs.data.map { it[USER_NAME_KEY] }.firstOrNull()
            _userName.value = savedName ?: ""

            val savedUsername = prefs.data.map { it[GITHUB_USERNAME_KEY] }.firstOrNull()
            if (!savedUsername.isNullOrBlank()) {
                _activeUsername.value = savedUsername
                gitHubRepository.getSignal(savedUsername)
            }
        }
    }

    val uiState: StateFlow<HomeUiState> = combine(
        projectRepository.getAllProjects(),
        signalFlow,
        stackRepository.getAllTech()
    ) { projects, signal, stack ->
        val active = projects.filter { it.status == ProjectStatus.BUILDING }.take(3)

        val devType = if (signal != null) {
            generateDeveloperType(signal, projects, stack)
        } else {
            "Add your GitHub username to see your identity"
        }

        val pulse = signal?.let {
            "Last pushed ${it.lastPushedDaysAgo}d ago · ${it.activeRepoCountThisMonth} repos active this month"
        } ?: ""

        HomeUiState(
            developerType = devType,
            activeProjects = active,
            pulseText = pulse,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState()
    )

    fun refreshGitHubSignal() {
        viewModelScope.launch {
            val savedUsername = prefs.data.map { it[GITHUB_USERNAME_KEY] }.firstOrNull()
            if (!savedUsername.isNullOrBlank()) {
                gitHubRepository.getSignal(savedUsername)
            }
        }
    }

    private fun generateDeveloperType(
        signal: GitHubSignal,
        projects: List<Project>,
        stack: List<TechEntry>
    ): String {
        val topLang = signal.topLanguages.firstOrNull()?.language ?: "code"
        val isKmp = stack.any {
            it.name.contains("KMP", ignoreCase = true) &&
                it.status == TechStatus.SHIPPED_WITH
        }
        val shippedCount = projects.count { it.status == ProjectStatus.SHIPPED }
        val lastPushed = signal.lastPushedDaysAgo ?: 99
        val isActive = lastPushed < 7

        return buildString {
            if (isKmp) append("KMP developer. ")
            else append("$topLang developer. ")

            if (isActive && signal.activeRepoCountThisMonth >= 2)
                append("Shipping actively. ")
            else if (isActive)
                append("Building actively. ")
            else
                append("Between projects. ")

            when {
                shippedCount >= 5 -> append("$shippedCount shipped projects.")
                shippedCount >= 2 -> append("$shippedCount shipped. More in progress.")
                shippedCount == 1 -> append("First ship done.")
                else -> append("First project underway.")
            }
        }
    }
}
