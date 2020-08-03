package com.kevalpatel2106.yip.splash

import androidx.appcompat.app.AppCompatDelegate
import androidx.hilt.lifecycle.ViewModelInject
import com.kevalpatel2106.yip.core.BaseViewModel
import com.kevalpatel2106.yip.repo.billingRepo.BillingRepo
import com.kevalpatel2106.yip.repo.nightModeRepo.NightModeRepo

internal class SplashViewModel @ViewModelInject constructor(
    billingRepo: BillingRepo,
    nightModeRepo: NightModeRepo
) : BaseViewModel() {
    init {
        billingRepo.refreshPurchaseState()
        AppCompatDelegate.setDefaultNightMode(nightModeRepo.getNightModeSetting())
    }
}
