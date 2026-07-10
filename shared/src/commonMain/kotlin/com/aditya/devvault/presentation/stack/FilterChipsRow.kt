package com.aditya.devvault.presentation.stack

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aditya.devvault.presentation.DevVaultSpacing

@Composable
fun FilterChipsRow(
    selectedFilter: StackFilter,
    onFilterSelected: (StackFilter) -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = DevVaultSpacing.md, vertical = DevVaultSpacing.sm),
        horizontalArrangement = Arrangement.spacedBy(DevVaultSpacing.sm)
    ) {
        items(StackFilter.entries) { filter ->
            FilterChip(
                selected = filter == selectedFilter,
                onClick = { onFilterSelected(filter) },
                label = {
                    Text(filter.label())
                }
            )
        }
    }
}

private fun StackFilter.label(): String = when (this) {
    StackFilter.ALL -> "All"
    StackFilter.COMFORTABLE -> "Comfortable"
    StackFilter.LEARNING -> "Learning"
    StackFilter.SHIPPED_WITH -> "Shipped With"
    StackFilter.DROPPED -> "Dropped"
}