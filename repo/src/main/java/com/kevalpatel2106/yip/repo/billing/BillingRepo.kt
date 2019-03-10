package com.kevalpatel2106.yip.repo.billing

import android.app.Activity
import android.app.Application
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.kevalpatel2106.yip.repo.providers.SharedPrefsProvider
import com.kevalpatel2106.yip.repo.utils.RxSchedulers
import io.reactivex.Single
import io.reactivex.SingleEmitter
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class BillingRepo @Inject internal constructor(
    private val application: Application,
    private val sharedPrefsProvider: SharedPrefsProvider
) {

    fun refreshPurchaseState(activity: Activity, sku: String = SKU_ID) {
        prepareBillingClient(
            activity,
            PurchasesUpdatedListener { _, _ -> /* Do nothing */ }).apply {

            startConnection(object : BillingClientStateListener {

                override fun onBillingSetupFinished(@BillingClient.BillingResponse billingResponseCode: Int) {
                    if (isBillingCodeSuccess(billingResponseCode)) {
                        checkIsPurchased(this@apply, sku)
                    } else {
                        isPurchased.onNext(false)
                    }
                }

                override fun onBillingServiceDisconnected() {
                    // IAP service connection failed.
                    isPurchased.onNext(sharedPrefsProvider.getBoolFromPreference(IS_PRO_KEY, false))
                }
            })
        }
    }

    private fun checkIsPurchased(billingClient: BillingClient, sku: String) {
        // Go to offline caching
        // It's fast and synchronous
        val purchaseResult = billingClient.queryPurchases(BillingClient.SkuType.INAPP)
        if (isBillingCodeSuccess(purchaseResult.responseCode) &&
            purchaseResult.purchasesList?.isNotEmpty() == true
        ) {

            val isPro = purchaseResult.purchasesList?.find { it.sku == sku } != null
            sharedPrefsProvider.savePreferences(IS_PRO_KEY, isPro)
            isPurchased.onNext(isPro)
        } else {
            isPurchased.onNext(sharedPrefsProvider.getBoolFromPreference(IS_PRO_KEY, false))
        }

        // Go check and update online status
        billingClient.queryPurchaseHistoryAsync(BillingClient.SkuType.INAPP) { responseCode, purchasesList ->
            if (isBillingCodeSuccess(responseCode)) {

                // Update base on online status.
                val isPro = purchasesList?.find { it.sku == sku } != null
                sharedPrefsProvider.savePreferences(IS_PRO_KEY, isPro)
                isPurchased.onNext(isPro)
            } else {
                // If the check fails, it's okay. We'll do next time.
            }
        }
    }

    fun purchase(activity: Activity, sku: String = SKU_ID): Single<String> {
        return Single.create<Purchase> { emitter ->

            val billingClient: BillingClient = prepareBillingClient(
                activity = activity,
                purchasesUpdatedListener = PurchasesUpdatedListener { responseCode, purchases ->

                    if (isBillingCodeSuccess(responseCode)) {

                        val purchasedSku = purchases?.find { it.sku == sku }
                        if (purchasedSku != null) {
                            emitter.onSuccess(purchasedSku)
                        } else {
                            emitter.tryOnError(Exception("Purchase failed. Please try again."))
                        }
                    } else {

                        // If the item already owned...don't worry. We will consume it.
                        if (responseCode != BillingClient.BillingResponse.ITEM_ALREADY_OWNED) {
                            emitter.tryOnError(
                                Exception(
                                    getPaymentMessage(
                                        application,
                                        responseCode
                                    )
                                )
                            )
                        }
                        return@PurchasesUpdatedListener
                    }
                }
            )

            billingClient.startConnection(object : BillingClientStateListener {
                override fun onBillingSetupFinished(@BillingClient.BillingResponse responseCode: Int) {
                    if (!isBillingCodeSuccess(responseCode)) {
                        emitter.tryOnError(Exception(getPaymentMessage(application, responseCode)))
                    } else {
                        consumeOrPurchase(billingClient, emitter, sku, activity)
                    }
                }

                override fun onBillingServiceDisconnected() {
                    //IAP service connection failed.
                    emitter.tryOnError(
                        Exception(
                            getPaymentMessage(
                                application,
                                BillingClient.BillingResponse.SERVICE_DISCONNECTED
                            )
                        )
                    )
                }
            })
        }.doAfterSuccess {
            isPurchased.onNext(true)
        }.map {
            sku
        }.subscribeOn(RxSchedulers.main)
    }

    private fun consumeOrPurchase(
        billingClient: BillingClient,
        emitter: SingleEmitter<Purchase>,
        sku: String,
        activity: Activity
    ) {
        billingClient.queryPurchaseHistoryAsync(BillingClient.SkuType.INAPP) { queryCode, purchases ->
            if (!isBillingCodeSuccess(queryCode)) {
                emitter.tryOnError(Exception(getPaymentMessage(application, queryCode)))
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
                        BillingClient.BillingResponse.OK -> {
                            emitter.onSuccess(previousPurchasedProduct)
                        }
                        else -> {
                            emitter.tryOnError(
                                Exception(
                                    getPaymentMessage(
                                        application,
                                        consumeCode
                                    )
                                )
                            )
                        }
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
        // Initiate the purchase flow
        @Suppress("DEPRECATION")
        val purchaseParams = BillingFlowParams.newBuilder()
            .setSku(sku)
            .setType(BillingClient.SkuType.INAPP)
            .build()

        val launchCode = billingClient.launchBillingFlow(activity, purchaseParams)
        if (!isBillingCodeSuccess(launchCode)) {
            emitter.tryOnError(Exception(getPaymentMessage(application, launchCode)))
        }
    }

    fun isPurchased(): Boolean = isPurchased.value == true

    fun observeIsPurchased(): BehaviorSubject<Boolean> = isPurchased

    companion object {
        private val isPurchased = BehaviorSubject.createDefault<Boolean>(false)
        const val SKU_ID = "pro_101"
        const val IS_PRO_KEY = "is_pro"
    }
}