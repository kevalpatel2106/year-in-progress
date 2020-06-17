package com.kevalpatel2106.yip.repo.billingRepo

import android.annotation.SuppressLint
import android.content.Context
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.PurchasesUpdatedListener
import com.kevalpatel2106.yip.repo.R

internal fun prepareBillingClient(
    activity: Context,
    purchasesUpdatedListener: PurchasesUpdatedListener
): BillingClient {
    return BillingClient
        .newBuilder(activity)
        .setListener(purchasesUpdatedListener)
        .enablePendingPurchases()
        .build()
}

internal fun isBillingCodeSuccess(billingResult: BillingResult): Boolean {
    return billingResult.responseCode == BillingClient.BillingResponseCode.OK
}

@SuppressLint("SwitchIntDef,ComplexMethod")
internal fun getPaymentMessage(context: Context, billingResult: BillingResult): String {
    return when (billingResult.responseCode) {
        BillingClient.BillingResponseCode.OK -> {
            context.getString(R.string.billing_code_ok)
        }
        BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED -> {
            context.getString(R.string.billing_code_already_purchased)
        }
        BillingClient.BillingResponseCode.DEVELOPER_ERROR -> {
            context.getString(R.string.billing_code_dev_error)
        }
        BillingClient.BillingResponseCode.ITEM_UNAVAILABLE -> {
            context.getString(R.string.billing_code_item_unavailable)
        }
        BillingClient.BillingResponseCode.FEATURE_NOT_SUPPORTED -> {
            context.getString(R.string.billing_code_not_supported)
        }
        BillingClient.BillingResponseCode.BILLING_UNAVAILABLE -> {
            context.getString(R.string.billing_code_unavailable)
        }
        BillingClient.BillingResponseCode.SERVICE_UNAVAILABLE -> {
            context.getString(R.string.billing_code_service_unavailable)
        }
        BillingClient.BillingResponseCode.SERVICE_DISCONNECTED -> {
            context.getString(R.string.billing_code_service_disconnected)
        }
        BillingClient.BillingResponseCode.USER_CANCELED -> {
            context.getString(R.string.billing_code_user_cancel)
        }
        else -> context.getString(R.string.billing_code_generic_error)
    }
}
