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
    internal val viewState = MutableLiveData<SettingsFragmentViewState>(
        SettingsFragmentViewState.initialState()
    )

    init {
        billingRepo.observeIsPurchased()
            .doOnSubscribe {
                viewState.value = viewState.value?.copy(isBuyProClickable = false)
            }
            .doOnNext {
                viewState.value = viewState.value?.copy(isBuyProClickable = true)
            }
            .subscribe { purchased ->
                viewState.value = viewState.value?.copy(isBuyProVisible = !purchased)
            }
            .addTo(compositeDisposable)
    }

    internal fun refreshPurchaseState(activity: Activity) {
        billingRepo.refreshPurchaseState(activity)
    }
}