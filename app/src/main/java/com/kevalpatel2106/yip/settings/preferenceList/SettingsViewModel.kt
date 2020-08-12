package com.kevalpatel2106.yip.settings.preferenceList

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kevalpatel2106.yip.core.BaseViewModel
import com.kevalpatel2106.yip.core.RxSchedulers
import com.kevalpatel2106.yip.core.ext.addTo
import com.kevalpatel2106.yip.core.livedata.modify
import com.kevalpatel2106.yip.repo.billingRepo.BillingRepo
import com.kevalpatel2106.yip.repo.nightModeRepo.NightModeRepo

internal class SettingsViewModel @ViewModelInject constructor(
    private val nightModeRepo: NightModeRepo,
    private val billingRepo: BillingRepo
) : BaseViewModel() {
    private val _viewState = MutableLiveData<SettingsFragmentViewState>(
        SettingsFragmentViewState.initialState(nightModeRepo.getNightModeSetting())
    )
    internal val viewState: LiveData<SettingsFragmentViewState> = _viewState

    init {
        billingRepo.refreshPurchaseState()
        monitorPurchaseStatus()
        monitorDarkModeSettings()
    }

    private fun monitorPurchaseStatus() {
        billingRepo.observeIsPurchased()
            .subscribeOn(RxSchedulers.compute)
            .observeOn(RxSchedulers.main)
            .doOnSubscribe { _viewState.modify { copy(isBuyProClickable = false) } }
            .doOnNext { _viewState.modify { copy(isBuyProClickable = true) } }
            .subscribe { purchased -> _viewState.modify { copy(isBuyProVisible = !purchased) } }
            .addTo(compositeDisposable)
    }

    private fun monitorDarkModeSettings() {
        nightModeRepo.observeNightModeChanges()
            .subscribeOn(RxSchedulers.preference)
            .observeOn(RxSchedulers.main)
            .subscribe { _viewState.modify { copy(darkModeValue = it) } }
            .addTo(compositeDisposable)
    }
}
