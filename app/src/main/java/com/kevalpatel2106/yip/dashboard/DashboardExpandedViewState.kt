package com.kevalpatel2106.yip.dashboard

sealed class DashboardExpandedViewState
object DetailViewCollapsed : DashboardExpandedViewState()
data class DetailViewExpanded(val deadlineId: Long) : DashboardExpandedViewState()
