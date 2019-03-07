package com.kevalpatel2106.yip.payment

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class PaymentActivityViewStateTest {

    @Test
    fun checkInitialState() {
        val initialState = PaymentActivityViewState.initialState()
        Assert.assertTrue(initialState.upgradeButtonClickable)
    }

    @Test
    fun checkEquals() {
        val state = PaymentActivityViewState.initialState()
        val state1 = PaymentActivityViewState.initialState()
        val state2 = PaymentActivityViewState(false)

        Assert.assertEquals(state, state1)
        Assert.assertNotEquals(state, state2)
        Assert.assertNotEquals(state1, state2)
    }
}