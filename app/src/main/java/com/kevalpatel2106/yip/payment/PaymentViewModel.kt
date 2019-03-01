package com.kevalpatel2106.yip.payment

import android.app.Activity
import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.core.BaseViewModel
import com.kevalpatel2106.yip.core.SingleLiveEvent
import com.kevalpatel2106.yip.core.addTo
import com.kevalpatel2106.yip.repo.billing.BillingRepo
import javax.inject.Inject

internal class PaymentViewModel @Inject internal constructor(
        private val application: Application,
        private val billingRepo: BillingRepo
) : BaseViewModel() {
    val isPurchasing = MutableLiveData<Boolean>()
    internal val userMessage = SingleLiveEvent<String>()
    internal val closeActivity = MutableLiveData<Unit>()

    fun purchase(activity: Activity) {
        billingRepo.purchase(BillingRepo.SKU_ID, activity)
                .doOnSubscribe { isPurchasing.value = true }
                .doAfterTerminate { isPurchasing.value = false }
                .subscribe({
                    userMessage.value = application.getString(R.string.purchase_successful)
                    closeActivity.value = Unit
                }, {
                    userMessage.value = it.message
                })
                .addTo(compositeDisposable)
    }
}