package com.kevalpatel2106.yip.repo.billingRepo

import android.content.Context
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingResult
import com.kevalpatel2106.yip.repo.R
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@RunWith(Parameterized::class)
class BillingResponseExtKtTest(private val billingResponseCode: Int, private val message: String) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): ArrayList<Array<out Any?>> {
            return arrayListOf(
                arrayOf(BillingClient.BillingResponseCode.OK, "1"),
                arrayOf(BillingClient.BillingResponseCode.BILLING_UNAVAILABLE, "8"),
                arrayOf(BillingClient.BillingResponseCode.DEVELOPER_ERROR, "3"),
                arrayOf(BillingClient.BillingResponseCode.ERROR, "4"),
                arrayOf(BillingClient.BillingResponseCode.FEATURE_NOT_SUPPORTED, "5"),
                arrayOf(BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED, "2"),
                arrayOf(BillingClient.BillingResponseCode.ITEM_NOT_OWNED, "4"),
                arrayOf(BillingClient.BillingResponseCode.SERVICE_DISCONNECTED, "6"),
                arrayOf(BillingClient.BillingResponseCode.ITEM_UNAVAILABLE, "10"),
                arrayOf(BillingClient.BillingResponseCode.SERVICE_UNAVAILABLE, "7"),
                arrayOf(BillingClient.BillingResponseCode.USER_CANCELED, "9"),
                arrayOf(546, "4")   // Generic code
            )
        }
    }

    @Mock
    internal lateinit var context: Context

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        whenever(context.getString(R.string.billing_code_ok)).thenReturn("1")
        whenever(context.getString(R.string.billing_code_already_purchased)).thenReturn("2")
        whenever(context.getString(R.string.billing_code_dev_error)).thenReturn("3")
        whenever(context.getString(R.string.billing_code_generic_error)).thenReturn("4")
        whenever(context.getString(R.string.billing_code_not_supported)).thenReturn("5")
        whenever(context.getString(R.string.billing_code_service_disconnected))
            .thenReturn("6")
        whenever(context.getString(R.string.billing_code_service_unavailable)).thenReturn("7")
        whenever(context.getString(R.string.billing_code_unavailable)).thenReturn("8")
        whenever(context.getString(R.string.billing_code_user_cancel)).thenReturn("9")
        whenever(context.getString(R.string.billing_code_item_unavailable)).thenReturn("10")
    }

    @Test
    fun checkErrorMessagesForBillingCode() {
        assertEquals(
            getPaymentMessage(
                context,
                BillingResult.newBuilder().setResponseCode(billingResponseCode).build()
            ),
            message
        )
    }
}
