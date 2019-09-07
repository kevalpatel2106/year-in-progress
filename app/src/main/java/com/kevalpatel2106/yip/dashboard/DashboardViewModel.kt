package com.kevalpatel2106.yip.dashboard

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.core.BaseViewModel
import com.kevalpatel2106.yip.core.addTo
import com.kevalpatel2106.yip.core.getBackgroundGradient
import com.kevalpatel2106.yip.core.livedata.SignalLiveEvent
import com.kevalpatel2106.yip.core.livedata.SingleLiveEvent
import com.kevalpatel2106.yip.core.livedata.recall
import com.kevalpatel2106.yip.core.openPlayStorePage
import com.kevalpatel2106.yip.core.recyclerview.representable.EmptyRepresentable
import com.kevalpatel2106.yip.core.recyclerview.representable.ErrorRepresentable
import com.kevalpatel2106.yip.core.recyclerview.representable.LoadingRepresentable
import com.kevalpatel2106.yip.core.recyclerview.representable.YipItemRepresentable
import com.kevalpatel2106.yip.core.updateWidgets
import com.kevalpatel2106.yip.dashboard.adapter.adsType.AdsItem
import com.kevalpatel2106.yip.dashboard.adapter.paddingType.PaddingItem
import com.kevalpatel2106.yip.dashboard.adapter.progressType.ProgressListItem
import com.kevalpatel2106.yip.entity.Progress
import com.kevalpatel2106.yip.repo.YipRepo
import com.kevalpatel2106.yip.repo.billing.BillingRepo
import com.kevalpatel2106.yip.repo.providers.SharedPrefsProvider
import com.kevalpatel2106.yip.utils.AppShortcutHelper
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
    private val billingRepo: BillingRepo,
    private val appShortcutHelper: AppShortcutHelper
) : BaseViewModel() {
    private val _progresses = MutableLiveData<ArrayList<YipItemRepresentable>>(arrayListOf())
    internal val progresses: LiveData<ArrayList<YipItemRepresentable>> = _progresses

    private val _askForRatingSignal = SignalLiveEvent()
    internal val askForRatingSignal: LiveData<Unit> = _askForRatingSignal

    private val _showInterstitialAdSignal = SignalLiveEvent()
    internal val showInterstitialAdSignal: LiveData<Unit> = _showInterstitialAdSignal

    private val _userMessages = SingleLiveEvent<String>()
    internal val userMessages: LiveData<String> = _userMessages

    private var _expandProgress = MutableLiveData<Long>(RESET_COLLAPSED_ID)
    internal var expandProgress: LiveData<Long> = _expandProgress

    init {
        monitorProgresses()
    }

    private fun monitorProgresses() {
        Flowable.combineLatest(
            yipRepo.observeAllProgress(),
            billingRepo.observeIsPurchased().toFlowable(BackpressureStrategy.BUFFER),
            BiFunction<List<Progress>, Boolean, Pair<List<Progress>, Boolean>> { list, isPro -> list to isPro }
        ).doOnSubscribe {
            // Show the loader.
            _progresses.value?.clear()
            _progresses.value?.add(LoadingRepresentable)
            _progresses.recall()
        }.doOnNext { (progresses, _) ->
            appShortcutHelper.updateDynamicShortcuts(progresses)
        }.map { (progresses, isPro) ->

            // Add Ads if the user is not pro.
            @Suppress("UNCHECKED_CAST")
            val list = progresses.map {
                ProgressListItem(
                    progress = it,
                    progressString = application.getString(
                        R.string.progress_percentage,
                        it.percent
                    ),
                    backgroundGradient = application.getBackgroundGradient(it.color.colorInt)
                )
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
            _progresses.value?.clear()
            if (listItems.isEmpty()) {
                // Show the empty list view.
                _progresses.value?.add(
                    EmptyRepresentable(
                        application.getString(R.string.dashboard_no_progress_message)
                    )
                )
            } else {
                // Show all the progress.
                listItems.add(PaddingItem)
                _progresses.value?.addAll(listItems)
            }
            _progresses.recall()
        }, { throwable ->
            Timber.e(throwable)

            // Display error.
            _progresses.value?.add(
                ErrorRepresentable(
                    application.getString(R.string.dashboard_error_loading_progress)
                ) {
                    monitorProgresses()
                })
        }).addTo(compositeDisposable)
    }

    internal fun userWantsToRateNow() {
        application.openPlayStorePage()
        sharedPrefsProvider.savePreferences(PREF_KEY_NEVER_ASK_RATE, true)
    }

    internal fun userWantsToNeverRate() {
        sharedPrefsProvider.savePreferences(PREF_KEY_NEVER_ASK_RATE, true)
    }

    internal fun userWantsToOpenDetail(progressId: Long) {
        yipRepo.isProgressExist(progressId)
            .subscribe({ exist ->
                if (exist) {
                    _expandProgress.value = progressId
                    handleRandomEvents()
                } else {
                    _userMessages.value = application.getString(R.string.error_progress_not_exist)
                }
            }, { throwable ->
                Timber.e(throwable)
                _userMessages.value = application.getString(R.string.error_progress_not_exist)
            })
            .addTo(compositeDisposable)
    }

    private fun handleRandomEvents() {
        Random.nextInt(MAX_RANDOM_NUMBER).let { randomNum ->
            // Should show rating dialog?
            if (!sharedPrefsProvider.getBoolFromPreference(
                    PREF_KEY_NEVER_ASK_RATE,
                    false
                ) && randomNum == RANDOM_NUMBER_FOR_RATING
            ) {
                _askForRatingSignal.sendSignal()
            }

            // Should show ads?
            if (randomNum == RANDOM_NUMBER_FOR_AD && !billingRepo.isPurchased()) {
                _showInterstitialAdSignal.sendSignal()
            }
        }
    }

    internal fun isDetailExpanded(): Boolean = expandProgress.value != RESET_COLLAPSED_ID

    internal fun resetExpandedProgress() {
        _expandProgress.value = RESET_COLLAPSED_ID
    }

    companion object {
        private const val RESET_COLLAPSED_ID = -1L
        private const val MAX_POSITION_OF_AD = 4
        private const val MAX_RANDOM_NUMBER = 14
        private const val RANDOM_NUMBER_FOR_RATING = 1
        private const val RANDOM_NUMBER_FOR_AD = 0
        private const val PREF_KEY_NEVER_ASK_RATE = "pref_key_rated"
    }
}
