package com.aditya.devvault.presentation.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.aditya.devvault.presentation.DevVaultSpacing
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
    onSettingsClick: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsState()
    val userName by viewModel.userName.collectAsState()

    Column(
        Modifier.fillMaxSize().padding(horizontal = DevVaultSpacing.md).statusBarsPadding()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                if (userName.isNotBlank()) "Hey $userName" else "Hey!",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = onSettingsClick) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings"
                )
            }
        }

        Spacer(Modifier.height(DevVaultSpacing.md))

        if (state.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Card {
                Text(
                    state.developerType,
                    modifier = Modifier.padding(DevVaultSpacing.md),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(Modifier.height(DevVaultSpacing.md))
            Text("Active Projects", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(DevVaultSpacing.sm))

            if (state.activeProjects.isEmpty()) {
                Text(
                    text = "No active projects yet",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                state.activeProjects.forEach { project ->
                    Card(Modifier.padding(vertical = DevVaultSpacing.xs)) {
                        Text(
                            project.name,
                            modifier = Modifier.padding(DevVaultSpacing.md),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }

            Spacer(Modifier.height(DevVaultSpacing.md))
            if (state.pulseText.isNotBlank()) {
                Text(state.pulseText, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
