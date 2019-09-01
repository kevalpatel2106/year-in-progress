package com.kevalpatel2106.yip.payment

import android.app.Activity
import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.core.BaseViewModel
import com.kevalpatel2106.yip.core.SignalLiveData
import com.kevalpatel2106.yip.core.SingleLiveEvent
import com.kevalpatel2106.yip.core.addTo
import com.kevalpatel2106.yip.repo.billing.BillingRepo
import javax.inject.Inject

internal class PaymentViewModel @Inject internal constructor(
    private val application: Application,
    private val billingRepo: BillingRepo
) : BaseViewModel() {

    val viewState =
        MutableLiveData<PaymentActivityViewState>(PaymentActivityViewState.initialState())
    internal val userMessage = SingleLiveEvent<String>()
    internal val closeSignal = SignalLiveData()

    fun purchase(activity: Activity) {
        billingRepo.purchase(activity)
            .doOnSubscribe {
                viewState.value = viewState.value?.copy(
                    upgradeButtonClickable = false
                )
            }
            .doAfterTerminate {
                viewState.value = viewState.value?.copy(
                    upgradeButtonClickable = true
                )
            }
            .subscribe({
                userMessage.value = application.getString(R.string.purchase_successful)
                closeSignal.invoke()
            }, {
                userMessage.value = it.message
            })
            .addTo(compositeDisposable)
    }
}
