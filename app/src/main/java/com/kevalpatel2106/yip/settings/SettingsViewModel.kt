package com.kevalpatel2106.yip.settings

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import com.kevalpatel2106.yip.core.BaseViewModel
import com.kevalpatel2106.yip.core.addTo
import com.kevalpatel2106.yip.repo.billing.BillingRepo
import javax.inject.Inject

internal class SettingsViewModel @Inject internal constructor(
        private val billingRepo: BillingRepo
) : BaseViewModel() {
    internal val isPurchased = MutableLiveData<Boolean>()
    internal val isCheckingPurchases = MutableLiveData<Boolean>()

    init {
        isPurchased.value = false
        billingRepo.observeIsPurchased()
                .doOnSubscribe { isCheckingPurchases.value = true }
                .doOnNext { isCheckingPurchases.value = false }
                .subscribe { purchased -> isPurchased.value = purchased }
                .addTo(compositeDisposable)
    }

    internal fun refreshPurchaseState(activity: Activity) {
        billingRepo.refreshPurchaseState(BillingRepo.SKU_ID, activity)
    }
}