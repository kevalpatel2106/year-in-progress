package com.kevalpatel2106.yip.repo.billingRepo

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.SkuDetailsParams
import com.kevalpatel2106.yip.repo.sharedPrefs.SharedPrefsProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.Single
import io.reactivex.SingleEmitter
import javax.inject.Inject

internal class BillingRepoImpl @Inject constructor(
    @ApplicationContext private val application: Context,
    private val sharedPrefsProvider: SharedPrefsProvider
) : BillingRepo {

    private var isPurchasedLocal: Boolean
        get() = sharedPrefsProvider.getBoolFromPreference(IS_PRO_KEY, false)
        set(value) = sharedPrefsProvider.savePreferences(IS_PRO_KEY, value)

    override fun refreshPurchaseState(sku: String) {
        val billingClient = prepareBillingClient(
            application,
            PurchasesUpdatedListener { _, _ -> /* Do nothing */ }
        )
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (isBillingCodeSuccess(billingResult)) {
                    billingClient.checkIsPurchasedOffline(sku)
                    billingClient.checkIsPurchasedOnline(sku)
                }
            }

            override fun onBillingServiceDisconnected() = Unit
        })
    }

    private fun BillingClient.checkIsPurchasedOnline(sku: String) {
        queryPurchaseHistoryAsync(BillingClient.SkuType.INAPP) { responseCode, purchasesList ->
            if (isBillingCodeSuccess(responseCode)) {

                // Update base on online status.
                val isPro = purchasesList?.any { it.sku == sku } == true
                isPurchasedLocal = isPro
            }
        }
    }

    private fun BillingClient.checkIsPurchasedOffline(sku: String) {
        val purchaseResult = queryPurchases(BillingClient.SkuType.INAPP)
        if (isBillingCodeSuccess(purchaseResult.billingResult) &&
            purchaseResult.purchasesList?.isNotEmpty() == true
        ) {
            val isPro = purchaseResult.purchasesList?.any { it.sku == sku } == true
            isPurchasedLocal = isPro
        }
    }

    override fun purchase(activity: Activity, sku: String): Single<String> {
        return Single.create<Purchase> { emitter ->

            val billingClient = prepareBillingClient(
                activity = activity,
                purchasesUpdatedListener = PurchasesUpdatedListener { billingResult, purchases ->

                    if (isBillingCodeSuccess(billingResult)) {

                        val purchasedSku = purchases?.find { it.sku == sku }
                        if (purchasedSku != null) {
                            emitter.onSuccess(purchasedSku)
                        } else {
                            emitter.tryOnError(Exception("Purchase failed. Please try again."))
                        }
                    } else {
                        emitter.tryOnError(Exception(getPaymentMessage(application, billingResult)))
                        return@PurchasesUpdatedListener
                    }
                }
            )

            billingClient.startConnection(object : BillingClientStateListener {

                override fun onBillingSetupFinished(billingResult: BillingResult) {
                    if (isBillingCodeSuccess(billingResult)) {
                        billingClient.buyNewProduct(activity, sku, emitter)
                    } else {
                        emitter.tryOnError(Exception(getPaymentMessage(application, billingResult)))
                    }
                }

                override fun onBillingServiceDisconnected() {
                    //IAP service connection failed.
                    val serviceDisconnectErrorCode = BillingResult.newBuilder()
                        .setResponseCode(BillingClient.BillingResponseCode.SERVICE_DISCONNECTED)
                        .build()
                    emitter.tryOnError(
                        Exception(getPaymentMessage(application, serviceDisconnectErrorCode))
                    )
                }
            })
        }.doAfterSuccess { isPurchasedLocal = true }
            .map { sku }
    }

    private fun BillingClient.buyNewProduct(
        activity: Activity,
        sku: String,
        emitter: SingleEmitter<Purchase>
    ) {
        val params = SkuDetailsParams.newBuilder()
            .setSkusList(listOf(sku))
            .setType(BillingClient.SkuType.INAPP)
        querySkuDetailsAsync(params.build()) { billingResult, skuDetailsList ->

            if (!isBillingCodeSuccess(billingResult)) {
                emitter.tryOnError(Exception(getPaymentMessage(application, billingResult)))
                return@querySkuDetailsAsync
            }

            // Buy SKU
            val purchaseParams = BillingFlowParams.newBuilder()
                .setSkuDetails(skuDetailsList!![0])
                .build()
            val buyResult = launchBillingFlow(activity, purchaseParams)
            if (!isBillingCodeSuccess(buyResult)) {
                emitter.tryOnError(Exception(getPaymentMessage(application, buyResult)))
            }
        }
    }

    override fun isPurchased(): Boolean = isPurchasedLocal

    override fun observeIsPurchased() = sharedPrefsProvider.observeBoolFromPreference(IS_PRO_KEY)

    companion object {
        private const val IS_PRO_KEY = "is_pro"
    }
}
