package com.kevalpatel2106.yip.splash

import android.app.Activity
import com.kevalpatel2106.yip.core.BaseViewModel
import com.kevalpatel2106.yip.repo.billing.BillingRepo
import javax.inject.Inject

internal class SplashViewModel @Inject constructor(
    private val billingRepo: BillingRepo
) : BaseViewModel() {
    internal fun refreshPurchaseState(activity: Activity) =
        billingRepo.refreshPurchaseState(activity)
}
