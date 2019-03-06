package com.kevalpatel2106.yip.payment

data class PaymentActivityViewState(
        val upgradeButtonClickable: Boolean
) {
    companion object {
        fun initialState(): PaymentActivityViewState {
            return PaymentActivityViewState(
                    upgradeButtonClickable = true
            )
        }
    }
}