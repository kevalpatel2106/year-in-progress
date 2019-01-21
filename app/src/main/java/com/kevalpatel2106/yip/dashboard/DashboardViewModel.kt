package com.kevalpatel2106.yip.dashboard

import android.app.Activity
import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.base.EmptyRepresentable
import com.kevalpatel2106.yip.base.ErrorRepresentable
import com.kevalpatel2106.yip.base.LoadingRepresentable
import com.kevalpatel2106.yip.base.YipItemRepresentable
import com.kevalpatel2106.yip.core.BaseViewModel
import com.kevalpatel2106.yip.core.addTo
import com.kevalpatel2106.yip.core.recall
import com.kevalpatel2106.yip.core.updateWidgets
import com.kevalpatel2106.yip.dashboard.adapter.AdsItem
import com.kevalpatel2106.yip.dashboard.adapter.ProgressListItem
import com.kevalpatel2106.yip.entity.Progress
import com.kevalpatel2106.yip.repo.YipRepo
import com.kevalpatel2106.yip.repo.billing.BillingRepo
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.functions.BiFunction
import javax.inject.Inject

internal class DashboardViewModel @Inject constructor(
    private val application: Application,
    private val yipRepo: YipRepo,
    private val billingRepo: BillingRepo
) : BaseViewModel() {
    internal val progresses = MutableLiveData<ArrayList<YipItemRepresentable>>()

    init {
        progresses.value = arrayListOf()
        monitorProgresses()
    }

    internal fun refreshPurchaseState(activity: Activity) {
        billingRepo.refreshPurchaseState(BillingRepo.SKU_ID, activity)
    }

    private fun monitorProgresses() {
        Flowable.combineLatest(
            yipRepo.observeAllProgress(),
            BillingRepo.isPurchased.toFlowable(BackpressureStrategy.BUFFER),
            BiFunction<List<Progress>, Boolean, Pair<List<Progress>, Boolean>> { list, isPurchased -> list to isPurchased }
        ).doOnSubscribe {
            progresses.value?.clear()
            progresses.value?.add(LoadingRepresentable)
            progresses.recall()
        }.map { (progresses, isPro) ->
            @Suppress("UNCHECKED_CAST")
            val list = progresses.map {
                ProgressListItem(it)
            }.toMutableList() as ArrayList<YipItemRepresentable>

            return@map list.apply {
                // Add the ads if the user is not pro.
                if (isNotEmpty() && !isPro) add(AdsItem)
            }
        }.doOnNext {
            application.updateWidgets()
        }.subscribe({ listItems ->
            progresses.value?.clear()
            if (listItems.isEmpty()) {
                progresses.value?.add(EmptyRepresentable(application.getString(R.string.dashboard_no_progress_message)))
            } else {
                progresses.value?.addAll(listItems)
            }
            progresses.recall()
        }, {
            it.printStackTrace()
            progresses.value?.add(ErrorRepresentable(application.getString(R.string.dashboard_error_loading_progress)) {
                monitorProgresses()
            })
        }).addTo(compositeDisposable)
    }
}