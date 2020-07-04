package com.kevalpatel2106.yip.payment

sealed class PaymentSingleEvent
data class ShowUserMessage(val message: String, val closeScreen: Boolean) : PaymentSingleEvent()
