package com.aditya.devvault.presentation.github

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.aditya.devvault.domain.model.GitHubSignal
import com.aditya.devvault.presentation.DevVaultSpacing
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun GitHubScreen(
    viewModel: GitHubViewModel = koinViewModel()
){
    val uiState by viewModel.uiState.collectAsState()
    var usernameInput by remember { mutableStateOf("") }

    // Sync input field with loaded username when success occurs
    LaunchedEffect(uiState) {
        if (uiState is GitHubUiState.Success) {
            usernameInput = (uiState as GitHubUiState.Success).data.username
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Day 6: Username input at top
        OutlinedTextField(
            value = usernameInput,
            onValueChange = { usernameInput = it },
            label = { Text("GitHub Username") },
            modifier = Modifier.fillMaxWidth().padding(DevVaultSpacing.md),
            singleLine = true,
            trailingIcon = {
                Button(
                    onClick = { viewModel.onUsernameSubmitted(usernameInput) },
                    enabled = usernameInput.isNotBlank(),
                    modifier = Modifier.padding(end = DevVaultSpacing.sm)
                ) {
                    Text("Fetch")
                }
            }
        )

        Box(modifier = Modifier.weight(1f)) {
            when (val state = uiState) {
                is GitHubUiState.Empty -> {
                    EmptyState()
                }
                is GitHubUiState.Loading -> {
                    LoadingState()
                }
                is GitHubUiState.Success -> {
                    SuccessState(
                        signal = state.data,
                        isStale = state.isStale,
                        onRefresh = { viewModel.onRefresh() }
                    )
                }
                is GitHubUiState.Error -> {
                    ErrorState(
                        message = state.message,
                        onRetry = { viewModel.onRefresh() }
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Add your GitHub username to see your signal",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(DevVaultSpacing.md))
            Text(
                "Fetching your GitHub signal...",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun ErrorState(message: String, onRetry: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(DevVaultSpacing.md))
            Button(onClick = onRetry) {
                Text("Retry")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SuccessState(
    signal: GitHubSignal,
    isStale: Boolean,
    onRefresh: () -> Unit
) {
    PullToRefreshBox(
        isRefreshing = false,
        onRefresh = onRefresh
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = DevVaultSpacing.md)
        ) {

            // Stale banner
            if (isStale) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Offline. Showing cached data.",
                        modifier = Modifier.padding(DevVaultSpacing.md),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
                Spacer(modifier = Modifier.height(DevVaultSpacing.md))
            }

            // Consistency Score card
            SignalCard(title = "Consistency Score") {
                Text(
                    text = "${signal.consistencyScore}/100",
                    style = MaterialTheme.typography.displaySmall
                )
            }

            Spacer(modifier = Modifier.height(DevVaultSpacing.md))

            // Language breakdown card
            SignalCard(title = "Language Breakdown") {
                signal.topLanguages.forEach { lang ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = DevVaultSpacing.xs),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = lang.language,
                            modifier = Modifier.width(80.dp),
                            style = MaterialTheme.typography.bodyMedium
                        )
                        LinearProgressIndicator(
                            progress = { lang.percentage / 100f },
                            modifier = Modifier
                                .weight(1f)
                                .height(DevVaultSpacing.sm)
                                .clip(RoundedCornerShape(4.dp))
                        )
                        Text(
                            text = "${lang.percentage.toInt()}%",
                            modifier = Modifier.width(40.dp),
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.End
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(DevVaultSpacing.md))

            // Active repos card
            SignalCard(title = "Active Repos") {
                signal.activeRepos.forEach { repo ->
                    Text(
                        text = "• ${repo.name}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(vertical = DevVaultSpacing.xs)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(DevVaultSpacing.md))
        }
    }
}



@Composable
private fun SignalCard(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(DevVaultSpacing.md)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(DevVaultSpacing.sm))
            content()
        }
    }
}