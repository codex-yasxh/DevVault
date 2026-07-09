package com.aditya.devvault.presentation.stack

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aditya.devvault.data.repository.StackRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class StackViewModel(
    private val stackRepository: StackRepository
) : ViewModel() {

    private val selectedFilter = MutableStateFlow(StackFilter.ALL)

    val uiState: StateFlow<StackUiState> =
        combine(
            stackRepository.getAllTech(),
            selectedFilter
        ) { techEntries, filter ->

            val filtered = filter.status?.let { status ->
                techEntries.filter { it.status == status }
            } ?: techEntries

            if (filtered.isEmpty()) {
                StackUiState.Empty
            } else {
                val timeline = filtered
                    .groupBy { it.firstUsedMonthYear.take(4) }
                    .toSortedMap()
                    .map { (year, technologies) ->
                        TechTimelineGroup(
                            year = year,
                            technologies = technologies.sortedBy {
                                it.firstUsedMonthYear
                            }
                        )
                    }

                StackUiState.Success(timeline)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = StackUiState.Empty
        )

    fun updateFilter(filter: StackFilter) {
        selectedFilter.value = filter
    }
}