package com.kevalpatel2106.yip.dashboard

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.core.BaseViewModel
import com.kevalpatel2106.yip.core.SingleLiveEvent
import com.kevalpatel2106.yip.core.addTo
import com.kevalpatel2106.yip.core.openPlayStorePage
import com.kevalpatel2106.yip.core.recall
import com.kevalpatel2106.yip.core.updateWidgets
import com.kevalpatel2106.yip.dashboard.adapter.AdsItem
import com.kevalpatel2106.yip.dashboard.adapter.PaddingItem
import com.kevalpatel2106.yip.dashboard.adapter.ProgressListItem
import com.kevalpatel2106.yip.entity.Progress
import com.kevalpatel2106.yip.recyclerview.representable.EmptyRepresentable
import com.kevalpatel2106.yip.recyclerview.representable.ErrorRepresentable
import com.kevalpatel2106.yip.recyclerview.representable.LoadingRepresentable
import com.kevalpatel2106.yip.recyclerview.representable.YipItemRepresentable
import com.kevalpatel2106.yip.repo.YipRepo
import com.kevalpatel2106.yip.repo.billing.BillingRepo
import com.kevalpatel2106.yip.repo.providers.SharedPrefsProvider
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.functions.BiFunction
import timber.log.Timber
import javax.inject.Inject
import kotlin.random.Random

internal class DashboardViewModel @Inject constructor(
    private val application: Application,
    private val yipRepo: YipRepo,
    private val sharedPrefsProvider: SharedPrefsProvider,
    private val billingRepo: BillingRepo
) : BaseViewModel() {
    internal val progresses = MutableLiveData<ArrayList<YipItemRepresentable>>()
    internal val askForRating = MutableLiveData<Unit>()
    internal val showInterstitialAd = MutableLiveData<Unit>()
    internal val userMessage = SingleLiveEvent<String>()

    internal var expandProgress = MutableLiveData<Long>()

    init {
        expandProgress.value = -1
        progresses.value = arrayListOf()
        monitorProgresses()
    }

    private fun monitorProgresses() {
        Flowable.combineLatest(
            yipRepo.observeAllProgress(),
            billingRepo.observeIsPurchased().toFlowable(BackpressureStrategy.BUFFER),
            BiFunction<List<Progress>, Boolean, Pair<List<Progress>, Boolean>> { list, isPro -> list to isPro }
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
                if (isNotEmpty() && !isPro) {
                    add(
                        if (size > MAX_POSITION_OF_AD) MAX_POSITION_OF_AD else size,
                        AdsItem
                    )
                }
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
                // Show all the progress.
                listItems.add(PaddingItem)
                progresses.value?.addAll(listItems)
            }
            progresses.recall()
        }, { throwable ->
            Timber.e(throwable)

            // Display error.
            progresses.value?.add(ErrorRepresentable(application.getString(R.string.dashboard_error_loading_progress)) {
                monitorProgresses()
            })
        }).addTo(compositeDisposable)
    }

    internal fun userWantsToRateNow() {
        application.openPlayStorePage()
        sharedPrefsProvider.savePreferences(PREF_KEY_RATED, true)
    }

    internal fun userWantsToOpenDetail(progressId: Long) {
        yipRepo.isProgressExist(progressId)
            .subscribe({ exist ->
                if (exist) {
                    expandProgress.value = progressId

                    Random.nextInt(MAX_RANDOM_NUMBER).let { randomNum ->
                        // Should show rating dialog?
                        if (!sharedPrefsProvider.getBoolFromPreference(
                                PREF_KEY_RATED,
                                false
                            ) && randomNum == 1
                        ) {
                            askForRating.value = Unit
                        }

                        // Should show ads?
                        if (randomNum == 0 && !billingRepo.isPurchased()) {
                            showInterstitialAd.value = Unit
                        }
                    }
                } else {
                    userMessage.value = application.getString(R.string.error_progress_not_exist)
                }
            }, { throwable ->
                Timber.e(throwable)
                userMessage.value = application.getString(R.string.error_progress_not_exist)
            })
            .addTo(compositeDisposable)
    }

    internal fun isDetailExpanded(): Boolean = expandProgress.value ?: -1 > 0L

    companion object {
        private const val MAX_POSITION_OF_AD = 4
        private const val MAX_RANDOM_NUMBER = 9
        private const val PREF_KEY_RATED = "pref_key_rated"
    }
}
