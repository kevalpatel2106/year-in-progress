package com.kevalpatel2106.yip.repo.billingRepo

import android.app.Activity
import io.reactivex.Observable
import io.reactivex.Single

interface BillingRepo {
    fun refreshPurchaseState(sku: String = SKU_ID)
    fun purchase(activity: Activity, sku: String = SKU_ID): Single<String>
    fun isPurchased(): Boolean
    fun observeIsPurchased(): Observable<Boolean>

    companion object {
        const val SKU_ID = "pro_101"
    }
}
