package com.kevalpatel2106.yip.repo.billingRepo

import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingResult
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class BillingCodeSuccessTest(private val billingResponseCode: Int, private val isSuccess: Boolean) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): ArrayList<Array<out Any?>> {
            return arrayListOf(
                arrayOf(BillingClient.BillingResponseCode.OK, true),
                arrayOf(BillingClient.BillingResponseCode.BILLING_UNAVAILABLE, false),
                arrayOf(BillingClient.BillingResponseCode.DEVELOPER_ERROR, false),
                arrayOf(BillingClient.BillingResponseCode.ERROR, false),
                arrayOf(BillingClient.BillingResponseCode.FEATURE_NOT_SUPPORTED, false),
                arrayOf(BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED, false),
                arrayOf(BillingClient.BillingResponseCode.ITEM_NOT_OWNED, false),
                arrayOf(BillingClient.BillingResponseCode.ITEM_UNAVAILABLE, false),
                arrayOf(BillingClient.BillingResponseCode.SERVICE_DISCONNECTED, false),
                arrayOf(BillingClient.BillingResponseCode.SERVICE_UNAVAILABLE, false),
                arrayOf(BillingClient.BillingResponseCode.USER_CANCELED, false),
                arrayOf(546, false)   // Generic code
            )
        }
    }

    @Test
    fun checkErrorMessagesForBillingCode() {
        assertEquals(
            isSuccess,
            isBillingCodeSuccess(BillingResult.newBuilder().setResponseCode(billingResponseCode).build())
        )
    }
}

