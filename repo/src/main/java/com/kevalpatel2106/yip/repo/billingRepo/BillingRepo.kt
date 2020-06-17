package com.kevalpatel2106.yip.repo.billingRepo

import android.app.Activity
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject

interface BillingRepo {
    fun refreshPurchaseState(sku: String = SKU_ID)
    fun purchase(activity: Activity, sku: String = SKU_ID): Single<String>
    fun isPurchased(): Boolean
    fun observeIsPurchased(): BehaviorSubject<Boolean>

    companion object {
        const val SKU_ID = "pro_101"
    }
}
