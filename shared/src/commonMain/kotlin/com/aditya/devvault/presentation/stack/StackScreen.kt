package com.aditya.devvault.presentation.stack

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aditya.devvault.presentation.DevVaultSpacing
import com.aditya.devvault.presentation.EmptyState
import com.aditya.devvault.presentation.LoadingState
import org.koin.compose.viewmodel.koinViewModel
@Composable
fun StackScreen(
    viewModel: StackViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val selectedFilter by viewModel.selectedFilter.collectAsState()
    val isSheetVisible by viewModel.isSheetVisible.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = viewModel::onAddTechClicked
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Tech"
                )
            }
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            FilterChipsRow(
                selectedFilter = selectedFilter,
                onFilterSelected = viewModel::updateFilter
            )

            Spacer(modifier = Modifier.height(DevVaultSpacing.md))

            when (val state = uiState) {

                StackUiState.Loading -> LoadingState()

                StackUiState.Empty -> {
                    EmptyState(
                        title = "No technologies yet",
                        subtitle = "Tap + to add your first technology"
                    )
                }

                is StackUiState.Success -> {
                    TimelineSection(
                        timeline = state.timeline
                    )
                }
            }
        }
    }

    if (isSheetVisible) {
        AddTechSheet(
            onDismiss = { viewModel.onDismissSheet() },
            onSave = { entry -> viewModel.onSaveTech(entry) }
        )
    }
}