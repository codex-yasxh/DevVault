package com.aditya.devvault.presentation.stack

import com.aditya.devvault.domain.model.TechStatus

enum class StackFilter(val status: TechStatus?) {
    ALL(null),
    COMFORTABLE(TechStatus.COMFORTABLE),
    LEARNING(TechStatus.LEARNING),
    SHIPPED_WITH(TechStatus.SHIPPED_WITH),
    DROPPED(TechStatus.DROPPED)
}