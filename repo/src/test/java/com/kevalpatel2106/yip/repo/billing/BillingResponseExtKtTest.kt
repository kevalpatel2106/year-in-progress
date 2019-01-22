package com.kevalpatel2106.yip.repo.billing

import android.content.Context
import com.android.billingclient.api.BillingClient
import com.kevalpatel2106.yip.repo.R
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@RunWith(Parameterized::class)
class BillingResponseExtKtTest(private val billingResponseCode: Int, private val message: String) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): ArrayList<Array<out Any?>> {
            return arrayListOf(
                    arrayOf(BillingClient.BillingResponse.OK, "1"),
                    arrayOf(BillingClient.BillingResponse.BILLING_UNAVAILABLE, "8"),
                    arrayOf(BillingClient.BillingResponse.DEVELOPER_ERROR, "3"),
                    arrayOf(BillingClient.BillingResponse.ERROR, "4"),
                    arrayOf(BillingClient.BillingResponse.FEATURE_NOT_SUPPORTED, "5"),
                    arrayOf(BillingClient.BillingResponse.ITEM_ALREADY_OWNED, "2"),
                    arrayOf(BillingClient.BillingResponse.ITEM_NOT_OWNED, "4"),
                    arrayOf(BillingClient.BillingResponse.SERVICE_DISCONNECTED, "6"),
                    arrayOf(BillingClient.BillingResponse.SERVICE_UNAVAILABLE, "7"),
                    arrayOf(BillingClient.BillingResponse.USER_CANCELED, "9"),
                    arrayOf(546, "4")
            )
        }
    }

    @Mock
    internal lateinit var context: Context

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        Mockito.`when`(context.getString(R.string.billing_code_ok)).thenReturn("1")
        Mockito.`when`(context.getString(R.string.billing_code_already_purchased)).thenReturn("2")
        Mockito.`when`(context.getString(R.string.billing_code_dev_error)).thenReturn("3")
        Mockito.`when`(context.getString(R.string.billing_code_genaric_error)).thenReturn("4")
        Mockito.`when`(context.getString(R.string.billing_code_not_supported)).thenReturn("5")
        Mockito.`when`(context.getString(R.string.billing_code_service_disconnected)).thenReturn("6")
        Mockito.`when`(context.getString(R.string.billing_code_service_unavailable)).thenReturn("7")
        Mockito.`when`(context.getString(R.string.billing_code_unabvailable)).thenReturn("8")
        Mockito.`when`(context.getString(R.string.billing_code_user_cancel)).thenReturn("9")
    }

    @Test
    fun checkErrorMessagesForBillingCode() {
        assertEquals(getPaymentMessage(context, billingResponseCode), message)
    }
}