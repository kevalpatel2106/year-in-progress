package com.kevalpatel2106.yip.payment

import android.app.Activity
import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.core.BaseViewModel
import com.kevalpatel2106.yip.core.RxSchedulers
import com.kevalpatel2106.yip.core.addTo
import com.kevalpatel2106.yip.core.livedata.SingleLiveEvent
import com.kevalpatel2106.yip.core.livedata.modify
import com.kevalpatel2106.yip.repo.billingRepo.BillingRepo
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber

internal class PaymentViewModel @ViewModelInject internal constructor(
    @ApplicationContext private val application: Context,
    private val billingRepo: BillingRepo
) : BaseViewModel() {

    private val _viewState = MutableLiveData<PaymentActivityViewState>(
        PaymentActivityViewState.initialState()
    )
    val viewState: LiveData<PaymentActivityViewState> = _viewState

    private val _singleEvent = SingleLiveEvent<PaymentSingleEvent>()
    internal val singleEvent: LiveData<PaymentSingleEvent> = _singleEvent

    internal fun purchase(activity: Activity) {
        billingRepo.purchase(activity)
            .subscribeOn(RxSchedulers.main)
            .observeOn(RxSchedulers.main)
            .doOnSubscribe { _viewState.modify { copy(upgradeButtonClickable = false) } }
            .doAfterTerminate { _viewState.modify { copy(upgradeButtonClickable = true) } }
            .subscribe({
                _singleEvent.value = ShowUserMessage(
                    message = application.getString(R.string.purchase_successful),
                    closeScreen = true
                )
            }, {
                Timber.e(it)
                _singleEvent.value = ShowUserMessage(
                    message = it.message.orEmpty(),
                    closeScreen = false
                )
            })
            .addTo(compositeDisposable)
    }
}
