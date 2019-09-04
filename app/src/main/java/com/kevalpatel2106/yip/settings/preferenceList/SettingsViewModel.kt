package com.kevalpatel2106.yip.settings.preferenceList

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kevalpatel2106.yip.core.BaseViewModel
import com.kevalpatel2106.yip.core.addTo
import com.kevalpatel2106.yip.repo.billing.BillingRepo
import javax.inject.Inject

internal class SettingsViewModel @Inject internal constructor(
    private val billingRepo: BillingRepo
) : BaseViewModel() {
    private val _viewState = MutableLiveData<SettingsFragmentViewState>(
        SettingsFragmentViewState.initialState()
    )
    internal val viewState: LiveData<SettingsFragmentViewState> = _viewState

    init {
        monitorPurchaseStatus()
    }

    private fun monitorPurchaseStatus() {
        billingRepo.observeIsPurchased()
            .doOnSubscribe { _viewState.value = _viewState.value?.copy(isBuyProClickable = false) }
            .doOnNext { _viewState.value = _viewState.value?.copy(isBuyProClickable = true) }
            .subscribe { purchased ->
                _viewState.value = _viewState.value?.copy(isBuyProVisible = !purchased)
            }
            .addTo(compositeDisposable)
    }

    internal fun refreshPurchaseState(activity: Activity) {
        billingRepo.refreshPurchaseState(activity)
    }
}
