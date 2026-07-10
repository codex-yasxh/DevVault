package com.aditya.devvault.presentation.stack

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aditya.devvault.data.repository.StackRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class StackViewModel(
    private val stackRepository: StackRepository
) : ViewModel() {

    private val _selectedFilter = MutableStateFlow(StackFilter.ALL)
    val selectedFilter: StateFlow<StackFilter> = _selectedFilter.asStateFlow()

    private val _uiState = MutableStateFlow<StackUiState>(StackUiState.Loading)
    val uiState: StateFlow<StackUiState> = _uiState

    init {
        viewModelScope.launch {
            combine(
                stackRepository.getAllTech(),
                _selectedFilter
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
            }.collect { _uiState.value = it }
        }
    }

    fun updateFilter(filter: StackFilter) {
        _selectedFilter.value = filter
    }
}