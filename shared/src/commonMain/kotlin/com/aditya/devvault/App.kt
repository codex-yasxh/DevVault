package com.aditya.devvault

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.aditya.devvault.presentation.github.GitHubScreen
import com.aditya.devvault.presentation.home.HomeScreen
import com.aditya.devvault.presentation.navigation.Screen
import com.aditya.devvault.presentation.projects.ProjectDetailScreen
import com.aditya.devvault.presentation.projects.ProjectsScreen
import com.aditya.devvault.presentation.settings.SettingsScreen
import com.aditya.devvault.presentation.stack.StackScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    MaterialTheme {
        var selectedTab by remember { mutableStateOf<Screen>(Screen.Home) }
        var detailScreen by remember { mutableStateOf<Screen?>(null) }

        val currentScreen = detailScreen ?: selectedTab

        BackHandler(enabled = detailScreen != null) {
            detailScreen = null
        }

        Scaffold(
            bottomBar = {
                if (detailScreen == null) {
                    NavigationBar {
                        NavigationBarItem(
                            selected = selectedTab is Screen.Home,
                            onClick = { selectedTab = Screen.Home },
                            icon = { Icon(Icons.Default.Home, contentDescription = null) },
                            label = { Text("Home") }
                        )
                        NavigationBarItem(
                            selected = selectedTab is Screen.Projects,
                            onClick = { selectedTab = Screen.Projects },
                            icon = { Icon(Icons.Default.Folder, contentDescription = null) },
                            label = { Text("Projects") }
                        )
                        NavigationBarItem(
                            selected = selectedTab is Screen.GitHub,
                            onClick = { selectedTab = Screen.GitHub },
                            icon = { Icon(Icons.Default.Code, contentDescription = null) },
                            label = { Text("GitHub") }
                        )
                        NavigationBarItem(
                            selected = selectedTab is Screen.Stack,
                            onClick = { selectedTab = Screen.Stack },
                            icon = { Icon(Icons.Default.Layers, contentDescription = null) },
                            label = { Text("Stack") }
                        )
                    }
                }
            }
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                when (currentScreen) {
                    Screen.Home -> HomeScreen(
                        onSettingsClick = { detailScreen = Screen.Settings }
                    )
                    Screen.Projects -> ProjectsScreen(
                        onProjectClick = { projectId ->
                            detailScreen = Screen.ProjectDetail(projectId)
                        }
                    )
                    Screen.GitHub -> GitHubScreen()
                    Screen.Stack -> StackScreen()
                    is Screen.Settings -> {
                        Scaffold(
                            topBar = {
                                TopAppBar(
                                    title = { Text("Settings") },
                                    navigationIcon = {
                                        IconButton(onClick = { detailScreen = null }) {
                                            Icon(
                                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                                contentDescription = "Back"
                                            )
                                        }
                                    }
                                )
                            }
                        ) { innerPadding ->
                            Box(Modifier.padding(innerPadding)) {
                                SettingsScreen()
                            }
                        }
                    }
                    is Screen.ProjectDetail -> ProjectDetailScreen(
                        projectId = currentScreen.projectId,
                        onBack = { detailScreen = null }
                    )
                }
            }
        }
    }
}
