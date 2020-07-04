package com.kevalpatel2106.yip.detail

sealed class DetailSingleEvent
object CloseDetailScreen : DetailSingleEvent()
object OpenPopUpMenu : DetailSingleEvent()
data class ShowDeleteConfirmationDialog(val deadlineTitle: String) : DetailSingleEvent()
data class ShowUserMessage(val message: String, val closeScreen: Boolean) : DetailSingleEvent()
