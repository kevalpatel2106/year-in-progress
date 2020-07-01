package com.kevalpatel2106.yip.splash

import androidx.hilt.lifecycle.ViewModelInject
import com.kevalpatel2106.yip.core.BaseViewModel
import com.kevalpatel2106.yip.repo.billingRepo.BillingRepo

internal class SplashViewModel @ViewModelInject constructor(
    private val billingRepo: BillingRepo
) : BaseViewModel() {
    internal fun refreshPurchase() = billingRepo.refreshPurchaseState()
}
