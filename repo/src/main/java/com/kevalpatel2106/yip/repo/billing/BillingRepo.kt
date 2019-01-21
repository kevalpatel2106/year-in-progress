package com.kevalpatel2106.yip.repo.billing

import android.app.Activity
import android.app.Application
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.Purchase
import com.kevalpatel2106.yip.repo.utils.RxSchedulers
import io.reactivex.Single
import io.reactivex.SingleEmitter
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class BillingRepo @Inject internal constructor(private val application: Application) {

    fun refreshPurchaseState(sku: String, activity: Activity) {
        val billingClient: BillingClient = BillingClient
            .newBuilder(activity)
            .setListener { _, _ -> /* Do nothing */ }
            .build()

        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(@BillingClient.BillingResponse billingResponseCode: Int) {
                if (billingResponseCode != BillingClient.BillingResponse.OK) {
                    isPurchased.onNext(false)
                } else {
                    billingClient.queryPurchaseHistoryAsync(sku) { responseCode, purchasesList ->
                        if (responseCode != BillingClient.BillingResponse.OK) {
                            isPurchased.onNext(false)
                        } else {
                            val purchasedSku = purchasesList?.find { it.sku == sku }
                            isPurchased.onNext(purchasedSku != null)
                        }
                    }
                }
            }

            override fun onBillingServiceDisconnected() {
                //IAP service connection failed.
                isPurchased.onNext(false)
            }
        })
    }

    fun purchase(sku: String, activity: Activity): Single<String> {
        return Single.create<Purchase> { emitter ->
            val billingClient: BillingClient = BillingClient
                .newBuilder(activity)
                .setListener { responseCode, purchases ->
                    if (responseCode != BillingClient.BillingResponse.OK) {

                        // If the item already owned...don't worry. We will consume it.
                        if (responseCode != BillingClient.BillingResponse.ITEM_ALREADY_OWNED) {
                            emitter.tryOnError(Exception(getPaymentMessage(responseCode)))
                        }
                        return@setListener
                    } else {

                        val purchasedSku = purchases?.find { it.sku == sku }
                        if (purchasedSku != null) {
                            emitter.onSuccess(purchasedSku)
                        } else {
                            emitter.tryOnError(Exception("Purchase failed. Please try again."))
                        }
                    }
                }
                .build()

            billingClient.startConnection(object : BillingClientStateListener {
                override fun onBillingSetupFinished(@BillingClient.BillingResponse billingResponseCode: Int) {
                    if (billingResponseCode != BillingClient.BillingResponse.OK) {
                        emitter.tryOnError(Exception(getPaymentMessage(billingResponseCode)))
                        return
                    }
                    consumeOrPurchase(billingClient, emitter, sku, activity)
                }

                override fun onBillingServiceDisconnected() {
                    //IAP service connection failed.
                    emitter.tryOnError(Exception(getPaymentMessage(BillingClient.BillingResponse.SERVICE_DISCONNECTED)))
                }
            })
        }.doAfterSuccess {
            refreshPurchaseState(sku, activity)
        }.subscribeOn(RxSchedulers.main)
            .map { sku }
    }

    private fun consumeOrPurchase(
        billingClient: BillingClient,
        emitter: SingleEmitter<Purchase>,
        sku: String,
        activity: Activity
    ) {
        billingClient.queryPurchaseHistoryAsync(BillingClient.SkuType.INAPP) { queryCode, purchases ->
            if (queryCode != BillingClient.BillingResponse.OK) {
                emitter.tryOnError(Exception(getPaymentMessage(queryCode)))
                return@queryPurchaseHistoryAsync
            }

            // Check if this SKU purchased previously?
            val previousPurchasedProduct = purchases?.findLast { it.sku == sku }
            if (previousPurchasedProduct != null) {

                billingClient.consumeAsync(previousPurchasedProduct.purchaseToken) { consumeCode, _ ->
                    when (consumeCode) {
                        BillingClient.BillingResponse.ITEM_NOT_OWNED -> {
                            // Item is not to consume
                            buyNewProduct(billingClient, activity, sku, emitter)
                        }
                        BillingClient.BillingResponse.OK -> emitter.onSuccess(previousPurchasedProduct)
                        else -> emitter.tryOnError(Exception(getPaymentMessage(consumeCode)))
                    }
                }
            } else {
                // No product bought before.
                buyNewProduct(billingClient, activity, sku, emitter)
            }
        }
    }

    private fun buyNewProduct(
        billingClient: BillingClient,
        activity: Activity,
        sku: String,
        emitter: SingleEmitter<Purchase>
    ) {
        //Initiate the purchase flow
        @Suppress("DEPRECATION")
        val purchaseParams = BillingFlowParams.newBuilder()
            .setSku(sku)
            .setType(BillingClient.SkuType.INAPP)
            .build()

        val launchCode = billingClient.launchBillingFlow(activity, purchaseParams)
        if (launchCode != BillingClient.BillingResponse.OK) {
            emitter.tryOnError(Exception(getPaymentMessage(launchCode)))
        }
    }

    companion object {
        val isPurchased = BehaviorSubject.create<Boolean>()
        const val SKU_ID = "pro_101"
    }
}