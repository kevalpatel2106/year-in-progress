package com.kevalpatel2106.yip.edit

sealed class EditDeadlineSingleViewState
data class ShowUserMessage(val message: String, val closeScreen: Boolean) :
    EditDeadlineSingleViewState()

object ShowNotificationPicker : EditDeadlineSingleViewState()
object OpenPaymentScreen : EditDeadlineSingleViewState()
object OpenEndDatePicker : EditDeadlineSingleViewState()
object OpenStartDatePicker : EditDeadlineSingleViewState()
object CloseScreen : EditDeadlineSingleViewState()
object ShowConfirmationDialog : EditDeadlineSingleViewState()
