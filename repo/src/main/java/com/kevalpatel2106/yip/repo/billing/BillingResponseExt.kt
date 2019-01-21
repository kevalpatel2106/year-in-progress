package com.kevalpatel2106.yip.repo.billing

import android.annotation.SuppressLint
import com.android.billingclient.api.BillingClient

@SuppressLint("SwitchIntDef")
internal fun getPaymentMessage(@BillingClient.BillingResponse responseCode: Int) = when (responseCode) {
    BillingClient.BillingResponse.OK -> "Transaction successful."
    BillingClient.BillingResponse.ITEM_ALREADY_OWNED -> "Item is already purchased."
    BillingClient.BillingResponse.DEVELOPER_ERROR -> "Something went wrong. Please contact developer."
    BillingClient.BillingResponse.ITEM_UNAVAILABLE -> "Invalid purchase item. Please contact developer."
    BillingClient.BillingResponse.FEATURE_NOT_SUPPORTED -> "Looks like your device doesn't support billing method. Please contact developer."
    BillingClient.BillingResponse.BILLING_UNAVAILABLE -> "Looks like your device doesn't support billing method. Please contact developer."
    BillingClient.BillingResponse.SERVICE_UNAVAILABLE -> "Cannot initiate the transaction. Check your network connection and try again."
    BillingClient.BillingResponse.SERVICE_DISCONNECTED -> "Billing interrupted. Don\'t worry, you aren\'t billed for purchase. Please try again."
    BillingClient.BillingResponse.USER_CANCELED -> "Billing interrupted by you. Please try again."
    else -> "Something went wrong. Please try again.."
}