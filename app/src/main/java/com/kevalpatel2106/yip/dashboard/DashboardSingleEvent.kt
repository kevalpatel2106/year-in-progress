package com.kevalpatel2106.yip.dashboard

sealed class DashboardSingleEvent
object ShowInterstitialAd : DashboardSingleEvent()
data class ShowUserMessage(val message: String) : DashboardSingleEvent()
data class OpenEditDeadline(val deadlineId: Long) : DashboardSingleEvent()
object OpenCreateNewDeadline : DashboardSingleEvent()
object CloseScreen : DashboardSingleEvent()
object OpenBottomNavigationSheet : DashboardSingleEvent()
