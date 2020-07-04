package com.kevalpatel2106.yip.payment

import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class PaymentActivityViewStateTest {

    @Test
    fun `check initial value of upgrade button is clickable`() {
        val initialState = PaymentActivityViewState.initialState()
        assertTrue(initialState.upgradeButtonClickable)
    }
}
