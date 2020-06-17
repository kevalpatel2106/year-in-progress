package com.kevalpatel2106.yip.splash

import com.kevalpatel2106.yip.core.BaseViewModel
import com.kevalpatel2106.yip.repo.billingRepo.BillingRepo
import javax.inject.Inject

internal class SplashViewModel @Inject constructor(
    private val billingRepo: BillingRepo
) : BaseViewModel() {
    internal fun refreshPurchase() = billingRepo.refreshPurchaseState()
}
