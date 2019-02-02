package com.kevalpatel2106.yip.repo.billing

import android.annotation.SuppressLint
import android.content.Context
import com.android.billingclient.api.BillingClient
import com.kevalpatel2106.yip.repo.R

@SuppressLint("SwitchIntDef")
internal fun getPaymentMessage(context: Context, @BillingClient.BillingResponse responseCode: Int) = when (responseCode) {
    BillingClient.BillingResponse.OK -> context.getString(R.string.billing_code_ok)
    BillingClient.BillingResponse.ITEM_ALREADY_OWNED -> context.getString(R.string.billing_code_already_purchased)
    BillingClient.BillingResponse.DEVELOPER_ERROR -> context.getString(R.string.billing_code_dev_error)
    BillingClient.BillingResponse.ITEM_UNAVAILABLE -> context.getString(R.string.billing_code_item_unavailable)
    BillingClient.BillingResponse.FEATURE_NOT_SUPPORTED -> context.getString(R.string.billing_code_not_supported)
    BillingClient.BillingResponse.BILLING_UNAVAILABLE -> context.getString(R.string.billing_code_unavailable)
    BillingClient.BillingResponse.SERVICE_UNAVAILABLE -> context.getString(R.string.billing_code_service_unavailable)
    BillingClient.BillingResponse.SERVICE_DISCONNECTED -> context.getString(R.string.billing_code_service_disconnected)
    BillingClient.BillingResponse.USER_CANCELED -> context.getString(R.string.billing_code_user_cancel)
    else -> context.getString(R.string.billing_code_generic_error)
}