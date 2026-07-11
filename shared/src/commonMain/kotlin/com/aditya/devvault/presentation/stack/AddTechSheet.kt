package com.aditya.devvault.presentation.stack

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.aditya.devvault.domain.model.TechEntry
import com.aditya.devvault.domain.model.TechStatus
import com.aditya.devvault.presentation.DevVaultSpacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTechSheet(
    onDismiss: () -> Unit,
    onSave: (TechEntry) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var status by remember { mutableStateOf(TechStatus.LEARNING) }
    var firstUsed by remember { mutableStateOf("") }
    var lastUsed by remember { mutableStateOf("") }

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(DevVaultSpacing.md)
                .verticalScroll(rememberScrollState())
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Technology Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(DevVaultSpacing.sm))

            Text(
                "Status",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(bottom = DevVaultSpacing.xs)
            )

            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                TechStatus.entries.forEachIndexed { index, techStatus ->
                    SegmentedButton(
                        selected = status == techStatus,
                        onClick = { status = techStatus },
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index,
                            count = TechStatus.entries.size
                        )
                    ) {
                        Text(techStatus.name)
                    }
                }
            }

            Spacer(modifier = Modifier.height(DevVaultSpacing.sm))

            OutlinedTextField(
                value = firstUsed,
                onValueChange = { firstUsed = it },
                label = { Text("First Used (YYYY-MM)") },
                placeholder = { Text("e.g. 2024-03") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(DevVaultSpacing.sm))

            OutlinedTextField(
                value = lastUsed,
                onValueChange = { lastUsed = it },
                label = { Text("Last Used (optional, YYYY-MM)") },
                placeholder = { Text("e.g. 2025-06") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(DevVaultSpacing.md))

            Button(
                onClick = {
                    if (name.isNotBlank() && firstUsed.isNotBlank()) {
                        onSave(
                            TechEntry(
                                id = 0L,
                                name = name.trim(),
                                status = status,
                                firstUsedMonthYear = firstUsed.trim(),
                                lastUsedMonthYear = lastUsed.trim().ifBlank { null }
                            )
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = name.isNotBlank() && firstUsed.isNotBlank()
            ) {
                Text("Save")
            }

            Spacer(modifier = Modifier.height(DevVaultSpacing.lg))
        }
    }
}
