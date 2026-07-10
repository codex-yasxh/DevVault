package com.aditya.devvault.presentation.stack

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aditya.devvault.presentation.DevVaultSpacing

@Composable
fun TimelineSection(
    timeline: List<TechTimelineGroup>
) {
    LazyColumn(
        contentPadding = PaddingValues(vertical = DevVaultSpacing.md)
    ) {
        items(
            items = timeline,
            key = { it.year }
        ) { group ->

            Column {

                Text(
                    text = group.year,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(horizontal = DevVaultSpacing.md)
                )

                Spacer(modifier = Modifier.height(DevVaultSpacing.sm))

                LazyRow(
                    contentPadding = PaddingValues(horizontal = DevVaultSpacing.md)
                ) {
                    items(
                        items = group.technologies,
                        key = { it.id }
                    ) { tech ->

                        TechChip(
                            tech = tech
                        )
                    }
                }

                Spacer(modifier = Modifier.height(DevVaultSpacing.md))
            }
        }
    }
}

