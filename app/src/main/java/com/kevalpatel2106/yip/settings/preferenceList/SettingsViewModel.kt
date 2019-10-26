package com.kevalpatel2106.yip.settings.preferenceList

import android.app.Activity
import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.core.BaseViewModel
import com.kevalpatel2106.yip.core.addTo
import com.kevalpatel2106.yip.repo.billingRepo.BillingRepo
import com.kevalpatel2106.yip.repo.utils.SharedPrefsProvider
import com.kevalpatel2106.yip.settings.SettingsUseCase
import javax.inject.Inject

internal class SettingsViewModel @Inject internal constructor(
    private val application: Application,
    private val sharedPrefsProvider: SharedPrefsProvider,
    private val billingRepo: BillingRepo
) : BaseViewModel() {
    private val _viewState = MutableLiveData<SettingsFragmentViewState>(
        SettingsFragmentViewState.initialState()
    )
    internal val viewState: LiveData<SettingsFragmentViewState> = _viewState

    init {
        monitorPurchaseStatus()
        monitorDarkModeSettings()
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

    private fun monitorDarkModeSettings() {
        sharedPrefsProvider.observeStringFromPreference(application.getString(R.string.pref_key_dark_mode))
            .subscribe { darkModeSettings ->
                AppCompatDelegate.setDefaultNightMode(
                    SettingsUseCase.getNightModeSettings(application, darkModeSettings)
                )
            }
            .addTo(compositeDisposable)
    }
}
