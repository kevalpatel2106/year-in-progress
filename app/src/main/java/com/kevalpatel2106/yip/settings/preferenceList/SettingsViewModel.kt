package com.kevalpatel2106.yip.settings.preferenceList

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.core.BaseViewModel
import com.kevalpatel2106.yip.core.RxSchedulers
import com.kevalpatel2106.yip.core.addTo
import com.kevalpatel2106.yip.repo.billingRepo.BillingRepo
import com.kevalpatel2106.yip.repo.sharedPrefs.SharedPrefsProvider
import com.kevalpatel2106.yip.settings.SettingsUseCase
import dagger.hilt.android.qualifiers.ApplicationContext

internal class SettingsViewModel @ViewModelInject internal constructor(
    @ApplicationContext private val application: Context,
    private val sharedPrefsProvider: SharedPrefsProvider,
    private val billingRepo: BillingRepo
) : BaseViewModel() {
    private val _viewState = MutableLiveData<SettingsFragmentViewState>(
        SettingsFragmentViewState.initialState()
    )
    internal val viewState: LiveData<SettingsFragmentViewState> = _viewState

    private val _darkModeSettings = MutableLiveData<Int>()
    internal val darkModeSettings: LiveData<Int> = _darkModeSettings

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

    internal fun refreshPurchaseState() = billingRepo.refreshPurchaseState()

    private fun monitorDarkModeSettings() {
        sharedPrefsProvider.observeStringFromPreference(application.getString(R.string.pref_key_dark_mode))
            .subscribeOn(RxSchedulers.preference)
            .observeOn(RxSchedulers.main)
            .distinctUntilChanged()
            .subscribe { darkModeSettings ->
                val darkModeSettingsInt = SettingsUseCase.getNightModeSettings(
                    context = application,
                    darkModeSettings = darkModeSettings
                )
                _darkModeSettings.value = darkModeSettingsInt
            }
            .addTo(compositeDisposable)
    }
}
