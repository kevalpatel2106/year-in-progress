package com.kevalpatel2106.yip.payment

import android.app.Activity
import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.core.BaseViewModel
import com.kevalpatel2106.yip.core.addTo
import com.kevalpatel2106.yip.core.livedata.SignalLiveEvent
import com.kevalpatel2106.yip.core.livedata.SingleLiveEvent
import com.kevalpatel2106.yip.repo.billingRepo.BillingRepo
import dagger.hilt.android.qualifiers.ApplicationContext

internal class PaymentViewModel @ViewModelInject internal constructor(
    @ApplicationContext private val application: Context,
    private val billingRepo: BillingRepo
) : BaseViewModel() {

    private val _viewState = MutableLiveData<PaymentActivityViewState>(
        PaymentActivityViewState.initialState()
    )
    val viewState: LiveData<PaymentActivityViewState> = _viewState

    private val _userMessage = SingleLiveEvent<String>()
    internal val userMessage: LiveData<String> = _userMessage

    private val _closeSignal = SignalLiveEvent()
    internal val closeSignal: LiveData<Unit> = _closeSignal

    internal fun purchase(activity: Activity) {
        billingRepo.purchase(activity)
            .doOnSubscribe {
                _viewState.value = viewState.value?.copy(upgradeButtonClickable = false)
            }
            .doAfterTerminate {
                _viewState.value = viewState.value?.copy(upgradeButtonClickable = true)
            }
            .subscribe({
                _userMessage.value = application.getString(R.string.purchase_successful)
                _closeSignal.sendSignal()
            }, {
                _userMessage.value = it.message
            })
            .addTo(compositeDisposable)
    }
}
