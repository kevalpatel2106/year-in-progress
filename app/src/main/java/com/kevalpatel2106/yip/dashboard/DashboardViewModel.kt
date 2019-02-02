package com.kevalpatel2106.yip.dashboard

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.core.BaseViewModel
import com.kevalpatel2106.yip.core.addTo
import com.kevalpatel2106.yip.core.recall
import com.kevalpatel2106.yip.core.updateWidgets
import com.kevalpatel2106.yip.dashboard.adapter.AdsItem
import com.kevalpatel2106.yip.dashboard.adapter.ProgressListItem
import com.kevalpatel2106.yip.entity.Progress
import com.kevalpatel2106.yip.recyclerview.representable.EmptyRepresentable
import com.kevalpatel2106.yip.recyclerview.representable.ErrorRepresentable
import com.kevalpatel2106.yip.recyclerview.representable.LoadingRepresentable
import com.kevalpatel2106.yip.recyclerview.representable.YipItemRepresentable
import com.kevalpatel2106.yip.repo.YipRepo
import com.kevalpatel2106.yip.repo.billing.BillingRepo
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.functions.BiFunction
import timber.log.Timber
import javax.inject.Inject

internal class DashboardViewModel @Inject constructor(
        private val application: Application,
        private val yipRepo: YipRepo
) : BaseViewModel() {
    internal val progresses = MutableLiveData<ArrayList<YipItemRepresentable>>()
    internal var expandedProgressId: Long = -1

    init {
        progresses.value = arrayListOf()
        monitorProgresses()
    }

    private fun monitorProgresses() {
        Flowable.combineLatest(
                yipRepo.observeAllProgress(),
                BillingRepo.isPurchased.toFlowable(BackpressureStrategy.BUFFER),
                BiFunction<List<Progress>, Boolean, Pair<List<Progress>, Boolean>> { list, isPurchased -> list to isPurchased }
        ).doOnSubscribe {
            // Show the loader.
            progresses.value?.clear()
            progresses.value?.add(LoadingRepresentable)
            progresses.recall()
        }.map { (progresses, isPro) ->

            // Add Ads if the user is not pro.
            @Suppress("UNCHECKED_CAST")
            val list = progresses.map {
                ProgressListItem(it)
            }.toMutableList() as ArrayList<YipItemRepresentable>

            return@map list.apply {
                // Add the ads if the user is not pro.
                if (isNotEmpty() && !isPro) add(if (size > 4) 4 else size, AdsItem)
            }
        }.doOnNext {
            // Update all widgets with new progress info
            application.updateWidgets()
        }.subscribe({ listItems ->
            progresses.value?.clear()
            if (listItems.isEmpty()) {
                // Show the empty list view.
                progresses.value?.add(EmptyRepresentable(application.getString(R.string.dashboard_no_progress_message)))
            } else {
                // Sow all the progress.
                progresses.value?.addAll(listItems)
            }
            progresses.recall()
        }, {
            Timber.e(it)
            it.printStackTrace()

            // Display error.
            progresses.value?.add(ErrorRepresentable(application.getString(R.string.dashboard_error_loading_progress)) {
                monitorProgresses()
            })
        }).addTo(compositeDisposable)
    }
}